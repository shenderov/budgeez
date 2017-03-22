package com.kamabizbazti.common;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"UnusedDeclaration", "unchecked"})
public class TestTools {

    private Gson gson = new Gson();

    public JsonObject objectToJsonObject(Object object) {
        if (object instanceof JsonObject)
            return (JsonObject) object;
        else
            return new JsonParser().parse(gson.toJson(object)).getAsJsonObject();
    }

    public Object stringToObject(String jsonString, Class returnClass) {
        return gson.fromJson(jsonString, returnClass);
    }

    public Object stringToObject(String jsonString, Type returnClass) {
        return gson.fromJson(jsonString, returnClass);
    }

    public String objectToJson(Object object) {
        if(object instanceof String)
            return (String) object;
        else
            return new JsonParser().parse(gson.toJson(object)).toString();
    }

    public Map<String, String> setToken(String token) {
        Map<String, String> headers = new HashMap<>();
        headers.put(TestConfiguration.TOKEN_HEADER, token);
        return headers;
    }

    public void sleep(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
