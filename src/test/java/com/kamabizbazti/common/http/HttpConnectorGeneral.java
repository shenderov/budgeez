package com.kamabizbazti.common.http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.kamabizbazti.common.TestConfiguration;
import com.kamabizbazti.model.entities.ChartRequestWrapper;
import com.kamabizbazti.model.entities.ChartSelection;
import com.kamabizbazti.model.entities.ChartWrapper;
import com.kamabizbazti.model.exceptions.UnknownSelectionIdException;
import com.kamabizbazti.model.interfaces.IGeneralRestController;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;

public class HttpConnectorGeneral implements IGeneralRestController {
    public HttpConnectorGeneral() {
        RestAssured.baseURI = TestConfiguration.BASE_URI;
        RestAssured.basePath = TestConfiguration.BASE_GENERAL_PATH;
    }

    private Gson gson = new Gson();

    public List<ChartSelection> getGeneralChartSelectionsList() {
        return getSelectionsList(TestConfiguration.GET_GENERAL_CHART_SELECTIONS_LIST);
    }

    public List<ChartSelection> getUserChartSelectionsList() {
        return getSelectionsList(TestConfiguration.GET_USER_CHART_SELECTIONS_LIST);
    }

    public ChartWrapper getDefaultDataTable() throws Exception {
        Response response;
        String jsonAsString;
        response =
                when().
                        get(TestConfiguration.GET_DEFAULT_DATATABLE).
                        then().
                        contentType(ContentType.JSON).
                        extract().response();
        jsonAsString = response.asString();
        return gson.fromJson(jsonAsString, ChartWrapper.class);
    }

    public ChartWrapper getGeneralDataTable(ChartRequestWrapper chartRequestWrapper) throws UnknownSelectionIdException {
        Response response;
        String jsonAsString;
        response =
                given().
                        body(chartRequestWrapper).
                        contentType(ContentType.JSON).
                when().
                        post(TestConfiguration.GET_GENERAL_DATATABLE).
                        then().
                        extract().response();
        jsonAsString = response.asString();
        return gson.fromJson(jsonAsString, ChartWrapper.class);
    }

    private List<ChartSelection> getSelectionsList(String path){
        Response response;
        String jsonAsString;
        Type listType = new TypeToken<ArrayList<ChartSelection>>(){}.getType();
        response =
                when().
                        get(path).
                        then().
                        contentType(ContentType.JSON).
                        extract().response();
        jsonAsString = response.asString();
        return gson.fromJson(jsonAsString, listType);
    }
}
