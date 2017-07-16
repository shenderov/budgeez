package com.budgeez.common.http;

import com.google.gson.reflect.TypeToken;
import com.budgeez.common.TestConfiguration;
import com.budgeez.common.entities.HttpResponse;
import com.budgeez.common.entities.HttpResponseJson;
import com.budgeez.model.entities.dao.GeneralCategory;
import com.budgeez.model.entities.dao.Record;
import com.budgeez.model.entities.external.*;

import java.lang.reflect.Type;
import java.util.List;

public class UserRestControllerConnectorHelper extends HttpConnectorGeneral {

    public UserRestControllerConnectorHelper(String hostname, int port, String basePath) {
        super(hostname, port, basePath);
    }

    public HttpResponse getCategoriesListPositive(String token) {
        Type listType = new TypeToken<List<GeneralCategory>>(){}.getType();
        HttpResponse response = sendGetRequest(TestConfiguration.GET_CATEGORIES_LIST, token);
        response.setObject(testTools.stringToObject((String) response.getObject(), listType));
        return response;
    }

    public HttpResponse getCategoriesListNegative(String token) {
        return sendGetRequest(TestConfiguration.GET_CATEGORIES_LIST, token);
    }

    public HttpResponse getUserDefaultDataTablePositive(String token){
        HttpResponse response = sendGetRequest(TestConfiguration.GET_USER_DEFAULT_DATATABLE, token);
        response.setObject(testTools.stringToObject((String) response.getObject(), ChartWrapper.class));
        return response;
    }

    public HttpResponse getUserDefaultDataTableNegative(String token){
        return sendGetRequest(TestConfiguration.GET_USER_DEFAULT_DATATABLE, token);
    }

    public HttpResponse getGeneralDataTablePositive(ChartRequestWrapper chartRequestWrapper, String token){
        HttpResponse response = sendPostRequest(TestConfiguration.GET_USER_DATATABLE, chartRequestWrapper, token);
        response.setObject(testTools.stringToObject((String) response.getObject(), ChartWrapper.class));
        return response;
    }

    public HttpResponse getGeneralDataTableNegative(String jsonString, String token){
        return sendPostRequest(TestConfiguration.GET_USER_DATATABLE, jsonString, token);
    }

    public HttpResponse addRecordPositive(ERecord record, String token) {
        HttpResponse response = sendPostRequest(TestConfiguration.ADD_RECORD, record, token);
        response.setObject(testTools.stringToObject((String) response.getObject(), Record.class));
        return response;
    }

    public HttpResponse addRecordNegative(String jsonString, String token) {
        return sendPostRequest(TestConfiguration.ADD_RECORD, jsonString, token);
    }

    public HttpResponse addCustomCategoryPositive(ECustomCategory customCategory, String token) {
        HttpResponse response = sendPostRequest(TestConfiguration.ADD_CUSTOM_CATEGORY, customCategory, token);
        response.setObject(testTools.stringToObject((String) response.getObject(), GeneralCategory.class));
        return response;
    }

    public HttpResponse addCustomCategoryNegative(String jsonString, String token) {
        return sendPostRequest(TestConfiguration.ADD_CUSTOM_CATEGORY, jsonString, token);
    }

    public HttpResponseJson getRecordsListJson(DatePicker datePicker, String token) {
        return sendPostRequest(TestConfiguration.GET_RECORDS_LIST, datePicker, token).convertToHttpResponseJson();
    }

    public HttpResponseJson getRecordsListJson(String request, String token) {
        return sendPostRequest(TestConfiguration.GET_RECORDS_LIST, request, token).convertToHttpResponseJson();
    }

    public HttpResponse deleteRecordPositive(Long recordId, String token) {
        HttpResponse response = sendPostRequest(TestConfiguration.DELETE_RECORD, recordId, token);
        response.setObject(testTools.stringToObject((String) response.getObject(), Boolean.class));
        return response;
    }

    public HttpResponse deleteRecordNegative(String jsonString, String token) {
        return sendPostRequest(TestConfiguration.DELETE_RECORD, jsonString, token);
    }

    public HttpResponse editRecordPositive(EditRecordWrapper record, String token) {
        HttpResponse response = sendPostRequest(TestConfiguration.EDIT_RECORD, record, token);
        response.setObject(testTools.stringToObject((String) response.getObject(), Record.class));
        return response;
    }

    public HttpResponse editRecordNegative(String jsonString, String token) {
        return sendPostRequest(TestConfiguration.EDIT_RECORD, jsonString, token);
    }
}
