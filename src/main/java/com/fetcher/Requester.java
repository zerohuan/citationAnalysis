package com.fetcher;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yjh on 2016/3/19.
 */
public class Requester {
    private CloseableHttpClient client;
    private Map<String, String> header;
    private String encoding;
    private String baseUrl;

    public Requester(CloseableHttpClient client) {
        this.client = client;
    }

    public ResponseData requestGet(Example example) throws IOException {
        ResponseData responseData = new ResponseData();
        HttpGet httpget = new HttpGet(example.url);
        if (!example.redirectAuto) {
            RequestConfig requestConfig = RequestConfig.custom()
                    .setRedirectsEnabled(false)
                    .build();
            httpget.setConfig(requestConfig);
        }
        if (header != null) {
            for(Map.Entry<String, String> entry : header.entrySet()) {
                httpget.setHeader(entry.getKey(), entry.getValue());
            }
        }
        if (example.tempHeader != null) {
            for(Map.Entry<String, String> entry : example.tempHeader.entrySet()) {
                httpget.setHeader(entry.getKey(), entry.getValue());
            }
        }
        try(CloseableHttpResponse response = client.execute(httpget)) {
            HttpEntity entity = response.getEntity();
            Header header;
            responseData.setLocation((header = response.getFirstHeader("Location")) == null ? null : header.getValue());
            responseData.setBody(EntityUtils.toString(entity, encoding).replace("&nbsp;",""));
        }
        return responseData;
    }

    public boolean downloadFile(Example example, File file) throws IOException {
        HttpGet httpget = new HttpGet(example.url);
        if (!example.redirectAuto) {
            RequestConfig requestConfig = RequestConfig.custom()
                    .setRedirectsEnabled(false)
                    .build();
            httpget.setConfig(requestConfig);
        }
        if (header != null) {
            for(Map.Entry<String, String> entry : header.entrySet()) {
                httpget.setHeader(entry.getKey(), entry.getValue());
            }
        }
        if (example.tempHeader != null) {
            for(Map.Entry<String, String> entry : example.tempHeader.entrySet()) {
                httpget.setHeader(entry.getKey(), entry.getValue());
            }
        }
        try(CloseableHttpResponse response = client.execute(httpget)) {
            InputStream inputStream = response.getEntity().getContent();
            OutputStream outStream = new FileOutputStream(file);
            IOUtils.copy(inputStream, outStream);
//            FileOutputStream outputStream = new FileOutputStream(file);
//
//            InputStream inputStream = response.getEntity().getContent();
//
//            byte buff[] = new byte[4096];
//            int counts;
//            System.out.println(".......");
//            while ((counts = inputStream.read(buff)) != -1) {
//                outputStream.write(buff, 0, counts);
//            }
//            outputStream.flush();
//            outputStream.close();
        }
        return true;
    }

    public ResponseData requestPost(Example example) throws IOException {
        ResponseData responseData = new ResponseData();
        HttpPost httpPost = new HttpPost(example.url);
        if (!example.redirectAuto) {
            RequestConfig requestConfig = RequestConfig.custom()
                    .setRedirectsEnabled(false)
                    .build();
            httpPost.setConfig(requestConfig);
        }
        if (header != null) {
            for(Map.Entry<String, String> entry : header.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }
        if (example.tempHeader != null) {
            for(Map.Entry<String, String> entry : example.tempHeader.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }
        if (example.params != null) {
            List<org.apache.http.NameValuePair> formparams = new ArrayList<>();
            for(Map.Entry<String, String> entry : example.params.entrySet()) {
                formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            UrlEncodedFormEntity uefEntity;
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httpPost.setEntity(uefEntity);
        }
        try(CloseableHttpResponse response = client.execute(httpPost)) {
            HttpEntity entity = response.getEntity();
            Header header;
            responseData.setLocation((header = response.getFirstHeader("Location")) == null ? null : header.getValue());
            responseData.setBody(EntityUtils.toString(entity, encoding).replace("&nbsp;",""));
        }
        return responseData;
    }

    public void close() throws IOException {
        this.client.close();
    }

    public Example createExample(String url) {
        return new Example(url);
    }

    public static void main(String[] args) throws IOException {
        RequesterBuilder builder = new RequesterBuilder();
        Requester requester = builder.setEncoding("utf8").build();
        System.out.println(requester.createExample("http://www.baidu.com").doGet());
    }



    public Requester addHeader(String name, String value) {
        if (header == null) {
            header = new HashMap<>();
        }
        header.put(name, value);
        return this;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public String toString() {
        return "Requester{" +
                "header=" + header +
                ", encoding='" + encoding + '\'' +
                ", baseUrl='" + baseUrl + '\'' +
                '}';
    }

    public class Example {
        private String url;
        private Map<String, String> tempHeader;
        private boolean redirectAuto = true;
        private Map<String, String> params;

        public Example(String url) {
            this.url = url;
        }

        public ResponseData doGet() throws IOException {
            System.out.println(url);
            return Requester.this.requestGet(this);
        }

        public ResponseData doPost() throws IOException {
            System.out.println(url);
            return Requester.this.requestPost(this);
        }

        public boolean doDownload(File file) throws IOException {
            System.out.println("download:" + url);
            return Requester.this.downloadFile(this, file);
        }

        public Example setUrl(String url) {
            this.url = url;
            return this;
        }

        public Example addHeader(String name, String value) {
            if (tempHeader == null)
                tempHeader = new HashMap<>();
            tempHeader.put(name, value);
            return this;
        }

        public Example addHeader(Map<String, String> map) {
            if (tempHeader == null)
                tempHeader = new HashMap<>();
            tempHeader.putAll(map);
            return this;
        }

        public Example setHeader(Map<String, String> tempHeader) {
            this.tempHeader = tempHeader;
            return this;
        }

        public Example addParam(String name, String value) {
            if (params == null)
                params = new HashMap<>();
            params.put(name, value);
            return this;
        }

        public Example setRedirectAuto(boolean redirectAuto) {
            this.redirectAuto = redirectAuto;
            return this;
        }
    }
}
