package com.fetcher;

import java.util.Map;

/**
 * Created by yjh on 2016/3/19.
 */
public class ResponseData {
    private String body;
    private String location;
    private Map<String,String> responseHeader;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getLocation() {
        return location == null || location.equalsIgnoreCase("null") ? "" : location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "ResponseData{" +
                "body='" + body + '\'' +
                ", location='" + location + '\'' +
                ", responseHeader=" + responseHeader +
                '}';
    }
}
