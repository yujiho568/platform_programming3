package edu.pnu.myspring.web.http;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class UserResponse {
    private int statusCode;
    private String body;
    private Map headers;
    public UserResponse(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
        this.headers = new HashMap<>();
    }
    public int getStatusCode() {
        return statusCode;
    }
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public Map getHeaders() {
        return headers;
    }
    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }
    public String getHeadersAsString() {
        return headers.entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining("\n"));
    }
    @Override
    public String toString() {
        return "Status Code: " + statusCode + "\n" +
                "Headers:\n" + getHeadersAsString() + "\n" +
                "Body:\n" + body;
    }
}