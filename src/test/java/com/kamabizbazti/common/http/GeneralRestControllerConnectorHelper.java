package com.kamabizbazti.common.http;

import com.google.gson.reflect.TypeToken;
import com.kamabizbazti.common.TestConfiguration;
import com.kamabizbazti.common.entities.HttpResponse;
import com.kamabizbazti.model.entities.ChartRequestWrapper;
import com.kamabizbazti.model.entities.ChartSelection;
import com.kamabizbazti.model.entities.ChartWrapper;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class GeneralRestControllerConnectorHelper extends HttpConnectorGeneral {

    public GeneralRestControllerConnectorHelper(int port) {
        super(port);
    }

    public HttpResponse getGeneralChartSelectionsListPositive() {
        Type listType = new TypeToken<ArrayList<ChartSelection>>(){}.getType();
        HttpResponse response = sendGetRequest(TestConfiguration.GET_GENERAL_CHART_SELECTIONS_LIST);
        response.setObject(testTools.stringToObject((String) response.getObject(), listType));
        return response;
    }

    public HttpResponse getUserChartSelectionsListPositive() {
        Type listType = new TypeToken<ArrayList<ChartSelection>>(){}.getType();
        HttpResponse response = sendGetRequest(TestConfiguration.GET_USER_CHART_SELECTIONS_LIST);
        response.setObject(testTools.stringToObject((String) response.getObject(), listType));
        return response;
    }

    public HttpResponse getDefaultDataTablePositive() {
        HttpResponse response = sendGetRequest(TestConfiguration.GET_DEFAULT_DATATABLE);
        response.setObject(testTools.stringToObject((String) response.getObject(), ChartWrapper.class));
        return response;
    }

    public HttpResponse getGeneralDataTablePositive(ChartRequestWrapper chartRequestWrapper) {
        HttpResponse response = sendPostRequest(TestConfiguration.GET_GENERAL_DATATABLE, chartRequestWrapper);
        response.setObject(testTools.stringToObject((String) response.getObject(), ChartWrapper.class));
        return response;
    }

    public HttpResponse getGeneralDataTableNegative(String jsonObject) {
        return sendPostRequest(TestConfiguration.GET_GENERAL_DATATABLE, jsonObject);
    }
}
