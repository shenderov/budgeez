package com.kamabizbazti.common.http;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.kamabizbazti.common.TestConfiguration;
import com.kamabizbazti.common.TestTools;
import com.kamabizbazti.common.entities.HttpResponse;

import java.util.HashMap;
import java.util.Map;

import static com.jayway.restassured.RestAssured.given;
@SuppressWarnings({"UnusedDeclaration", "unchecked", "FieldCanBeLocal"})
public class HttpConnectorGeneral {
    HttpConnectorGeneral(int port) {
        if (System.getProperty("baseURI") != null)
            RestAssured.baseURI = System.getProperty("baseURI");
        else
            RestAssured.baseURI = TestConfiguration.BASE_URI;
        if (System.getProperty("port") != null)
            RestAssured.port = Integer.parseInt(System.getProperty("port"));
        else
            RestAssured.port = port;
        if (System.getProperty("basePath") != null)
            RestAssured.basePath = System.getProperty("basePath");
        else
            RestAssured.basePath = TestConfiguration.BASE_PATH;
    }

    protected TestTools testTools = new TestTools();

    HttpResponse sendPostRequest(String api, String request, Map<String, String> headers) {
        Response response = sendHttpPostRequest(api, request, headers);
        return new HttpResponse(response.asString(), response);
    }

    HttpResponse sendPostRequest(String api, String request, String token) {
        Response response = sendHttpPostRequest(api, request, testTools.setToken(token));
        return new HttpResponse(response.asString(), response);
    }

    HttpResponse sendPostRequest(String api, String request) {
        Response response = sendHttpPostRequest(api, request, new HashMap<>());
        return new HttpResponse(response.asString(), response);
    }

    HttpResponse sendPostRequest(String api, Object request, Map<String, String> headers) {
        Response response = sendHttpPostRequest(api, testTools.ObjectToJson(request), headers);
        return new HttpResponse(response.asString(), response);
    }

    HttpResponse sendPostRequest(String api, Object request, String token) {
        Response response = sendHttpPostRequest(api, testTools.ObjectToJson(request), testTools.setToken(token));
        return new HttpResponse(response.asString(), response);
    }

    HttpResponse sendPostRequest(String api, Object request) {
        Response response = sendHttpPostRequest(api, testTools.ObjectToJson(request), new HashMap<>());
        return new HttpResponse(response.asString(), response);
    }

    HttpResponse sendGetRequest(String api, Map<String, String> headers) {
        Response response = sendHttpGetRequest(api, headers);
        return new HttpResponse(response.asString(), response);
    }

    HttpResponse sendGetRequest(String api, String token) {
        Response response = sendHttpGetRequest(api, testTools.setToken(token));
        return new HttpResponse(response.asString(), response);
    }

    HttpResponse sendGetRequest(String api) {
        Response response = sendHttpGetRequest(api, new HashMap<>());
        return new HttpResponse(response.asString(), response);
    }

    private Response sendHttpGetRequest(String api, Map<String, String> headers) {
        Response response;
        response =
                given().
                        headers(headers).
                        when().
                        get(api).
                        then().
                        contentType(ContentType.JSON).
                        extract().response();
        return response;
    }

    private Response sendHttpPostRequest(String api, Object request, Map<String, String> headers) {
        Response response;
        response =
                given().
                        body(request).
                        headers(headers).
                        contentType(ContentType.JSON).
                        when().
                        post(api).
                        then().
                        extract().response();
        return response;
    }
}
