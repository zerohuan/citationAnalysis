package com.fetcher;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.protocol.HttpContext;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * 生成者模式创建请求对象
 * 线程不安全
 * Created by yjh on 2016/3/19.
 */
public class RequesterBuilder {
    private Map<String,String> header;
    private String encoding;
    private String baseUrl;

    public Requester build() {
        HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler() {
            public boolean retryRequest(
                    IOException exception,
                    int executionCount,
                    HttpContext context) {
                if (executionCount >= 5) {
                    // Do not retry if over max retry count
                    return false;
                }
                if (exception instanceof InterruptedIOException) {
                    // Timeout
                    return false;
                }
                if (exception instanceof UnknownHostException) {
                    // Unknown host
                    return false;
                }
                if (exception instanceof ConnectTimeoutException) {
                    // Connection refused
                    return false;
                }
                if (exception instanceof SSLException) {
                    // SSL handshake exception
                    return false;
                }
                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
                if (idempotent) {
                    // Retry if the request is considered idempotent
                    return true;
                }
                return false;
            }
        };
        LaxRedirectStrategy redirectStrategy = new LaxRedirectStrategy();
        CookieStore cookieStore = new BasicCookieStore();
        RequestConfig requestConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.STANDARD_STRICT)
                .setRedirectsEnabled(true)
                .setSocketTimeout(20000)
                .setConnectTimeout(10000).build();
        CloseableHttpClient client = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setRetryHandler(myRetryHandler)
                .setDefaultCookieStore(cookieStore)
                .setRedirectStrategy(redirectStrategy)
                .build();
        Requester requester = new Requester(client);
        requester.setHeader(header);
        requester.setEncoding(encoding);
        requester.setBaseUrl(baseUrl);
        return requester;
    }

    public RequesterBuilder addHeader(String name, String value) {
        if (header == null) {
            header = new HashMap<>();
        }
        header.put(name, value);
        return this;
    }

    public RequesterBuilder setHeader(Map<String, String> header) {
        this.header = header;
        return this;
    }

    public RequesterBuilder setEncoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    public RequesterBuilder setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }
}
