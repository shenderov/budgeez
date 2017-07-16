package com.budgeez.restcontroller;

import com.budgeez.BudgeezBootApplicationTests;
import com.budgeez.common.entities.HttpResponse;
import com.budgeez.common.entities.HttpResponseJson;
import com.budgeez.model.entities.dao.GeneralCategory;
import com.budgeez.model.entities.dao.Record;
import com.budgeez.model.entities.external.ChartRequestWrapper;
import com.budgeez.model.entities.external.DatePicker;
import com.budgeez.model.entities.external.ERecord;
import com.budgeez.model.enumerations.ChartSelectionIdEnum;
import com.budgeez.security.entities.SignUpWrapper;
import com.budgeez.model.entities.dao.User;
import com.budgeez.security.service.JwtAuthenticationResponse;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class UserRestControllerAuthenticationCheckTest extends BudgeezBootApplicationTests {

    private String name = "User Test Auth";
    private String email = "usertestauth@budgeez.com";
    private String password = "Password";
    private User user;
    private Record record;
    private String token;
    private String expiredToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0QGthbWFiaXpiYXp0aS5jb20iLCJhdWRpZW5jZSI6IndlYiIsImNyZWF0ZWQiOjE0NzkyMzMxNjA0OTcsImV4cCI6MTQ3OTIzMzM0MH0.WdTasVlFcNOzGiRA0gD-f7ExW-DcUWdiudozVmk_xGVU-Y3gzf7bPjNmJe5QALiwneHlsNrqVBnd5pqp6tlNzQ";

    @BeforeClass
    public void setup() {
        SignUpWrapper wrapper = new SignUpWrapper();
        wrapper.setName(name);
        wrapper.setEmail(email);
        wrapper.setPassword(password);
        JwtAuthenticationResponse token = (JwtAuthenticationResponse) authenticationRestControllerConnectorHelper.createUserPositive(wrapper).getObject();
        this.token = token.getToken();
        user = userRepository.findByUsername(email);
    }

    @AfterClass
    public void tearDown(){
        userRepository.delete(user.getId());
    }

    @Test
    public void testGetCategoriesList(){
        HttpResponseJson response = userRestControllerConnectorHelper.getCategoriesListNegative(null).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 401);
        assertEquals(response.getObject().get("error").getAsString(), "Unauthorized");
        assertEquals(response.getObject().get("message").getAsString(), "Unauthorized");
    }

    @Test
    public void testGetUserDefaultDataTable(){
        HttpResponseJson response = userRestControllerConnectorHelper.getUserDefaultDataTableNegative(null).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 401);
        assertEquals(response.getObject().get("error").getAsString(), "Unauthorized");
        assertEquals(response.getObject().get("message").getAsString(), "Unauthorized");
    }

    @Test
    public void testGetCategoriesListInvalidToken(){
        HttpResponseJson response = userRestControllerConnectorHelper.getCategoriesListNegative(expiredToken).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 401);
        assertEquals(response.getObject().get("error").getAsString(), "Unauthorized");
        assertEquals(response.getObject().get("message").getAsString(), "Unauthorized");
    }

    @Test
    public void testGetUserDefaultDataTableInvalidToken(){
        HttpResponseJson response = userRestControllerConnectorHelper.getUserDefaultDataTableNegative(expiredToken).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 401);
        assertEquals(response.getObject().get("error").getAsString(), "Unauthorized");
        assertEquals(response.getObject().get("message").getAsString(), "Unauthorized");
    }

    @Test
    public void testGetUserDataTable(){
        ChartRequestWrapper requestWrapper = new ChartRequestWrapper();
        requestWrapper.setChartSelection(chartSelectionRepository.findOne(ChartSelectionIdEnum.USER_CURRENT_MONTH));
        HttpResponseJson response = userRestControllerConnectorHelper.getGeneralDataTableNegative(testTools.objectToJson(requestWrapper),null).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 401);
        assertEquals(response.getObject().get("error").getAsString(), "Unauthorized");
        assertEquals(response.getObject().get("message").getAsString(), "Unauthorized");
    }

    @Test
    public void testAddRecord(){
        GeneralCategory category = generalCategoryRepository.findAll().get(0);
        ERecord record = new ERecord();
        record.setCategoryId(category.getCategoryId());
        record.setAmount(12.5);
        record.setDate(System.currentTimeMillis());
        HttpResponse httpResponse = userRestControllerConnectorHelper.addRecordPositive(record, token);
        this.record = (Record) httpResponse.getObject();
        HttpResponseJson response = userRestControllerConnectorHelper.addRecordNegative(testTools.objectToJson(this.record),null).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 401);
        assertEquals(response.getObject().get("error").getAsString(), "Unauthorized");
        assertEquals(response.getObject().get("message").getAsString(), "Unauthorized");
    }

    @Test
    public void testCustomCategory(){
        GeneralCategory category = new GeneralCategory();
        category.setName("New category");
        HttpResponseJson response = userRestControllerConnectorHelper.addCustomCategoryNegative(testTools.objectToJson(category),null).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 401);
        assertEquals(response.getObject().get("error").getAsString(), "Unauthorized");
        assertEquals(response.getObject().get("message").getAsString(), "Unauthorized");
    }

    @Test
    public void testGetRecordsList(){
        DatePicker picker = new DatePicker();
        picker.setEndDate(System.currentTimeMillis());
        picker.setStartDate(System.currentTimeMillis()-100000);
        HttpResponseJson response = userRestControllerConnectorHelper.getRecordsListJson(picker,null);
        assertEquals(response.getHttpStatusCode(), 401);
        assertEquals(response.getObject().get("error").getAsString(), "Unauthorized");
        assertEquals(response.getObject().get("message").getAsString(), "Unauthorized");
    }

    @Test
    public void testDeleteRecord(){
        HttpResponseJson response = userRestControllerConnectorHelper.deleteRecordNegative("1",null).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 401);
        assertEquals(response.getObject().get("error").getAsString(), "Unauthorized");
        assertEquals(response.getObject().get("message").getAsString(), "Unauthorized");
    }

    @Test(dependsOnMethods = {"testAddRecord"})
    public void testEditRecord(){
        GeneralCategory category = generalCategoryRepository.findAll().get(0);
        record.setCategory(category);
        HttpResponseJson response = userRestControllerConnectorHelper.editRecordNegative(testTools.objectToJson(record),null).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 401);
        assertEquals(response.getObject().get("error").getAsString(), "Unauthorized");
        assertEquals(response.getObject().get("message").getAsString(), "Unauthorized");
    }
}
