package com.budgeez.common.entities;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jayway.restassured.response.Headers;
import com.jayway.restassured.response.Response;

import java.util.Map;

@SuppressWarnings({"UnusedDeclaration"})
public class HttpResponse {
    private Object object;
    private Map <String, String> cookies;
    private Headers headers;
    private int httpStatusCode;

    HttpResponse() {
        super();
    }

    HttpResponse(Object object, Map<String, String> cookies, Headers headers, int httpStatusCode) {
        this.object = object;
        this.cookies = cookies;
        this.headers = headers;
        this.httpStatusCode = httpStatusCode;
    }

    public HttpResponse(Object object, Response response) {
        this.object = object;
        this.cookies = response.getCookies();
        this.headers = response.getHeaders();
        this.httpStatusCode = response.getStatusCode();
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public Headers getHeaders() {
        return headers;
    }

    void setHeaders(Headers headers) {
        this.headers = headers;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public HttpResponseJson convertToHttpResponseJson(){
        JsonObject jsonObject;
        if(this.object instanceof String)
            jsonObject = new JsonParser().parse((String) this.object).getAsJsonObject();
        else
            jsonObject = new JsonParser().parse(new Gson().toJson(object)).getAsJsonObject();
        return new HttpResponseJson(jsonObject, this.cookies, this.headers, this.httpStatusCode);
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "object=" + object.toString() +
                ", cookies=" + cookies +
                ", headers=" + headers +
                ", httpStatusCode=" + httpStatusCode +
                '}';
    }
}
