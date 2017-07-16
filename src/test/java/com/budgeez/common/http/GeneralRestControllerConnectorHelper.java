package com.budgeez.common.http;

import com.google.gson.reflect.TypeToken;
import com.budgeez.common.TestConfiguration;
import com.budgeez.common.entities.HttpResponse;
import com.budgeez.model.entities.dao.Currency;
import com.budgeez.model.entities.dao.Language;
import com.budgeez.model.entities.external.ChartRequestWrapper;
import com.budgeez.model.entities.dao.ChartSelection;
import com.budgeez.model.entities.external.ChartWrapper;
import com.budgeez.model.entities.external.EVersion;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class GeneralRestControllerConnectorHelper extends HttpConnectorGeneral {

    public GeneralRestControllerConnectorHelper(String hostname, int port, String basePath) {
        super(hostname, port, basePath);
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

    public HttpResponse getVersion() {
        HttpResponse response = sendGetRequest(TestConfiguration.GET_VERSION);
        response.setObject(testTools.stringToObject((String) response.getObject(), EVersion.class));
        return response;
    }

    public HttpResponse getLanguages() {
        Type listType = new TypeToken<ArrayList<Language>>(){}.getType();
        HttpResponse response = sendGetRequest(TestConfiguration.GET_LANGUAGES);
        response.setObject(testTools.stringToObject((String) response.getObject(), listType));
        return response;
    }

    public HttpResponse getCurrencies() {
        Type listType = new TypeToken<ArrayList<Currency>>(){}.getType();
        HttpResponse response = sendGetRequest(TestConfiguration.GET_CURRENCIES);
        response.setObject(testTools.stringToObject((String) response.getObject(), listType));
        return response;
    }
}
