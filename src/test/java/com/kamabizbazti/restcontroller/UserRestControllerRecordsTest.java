package com.kamabizbazti.restcontroller;

import com.google.gson.JsonObject;
import com.kamabizbazti.KamaBizbaztiBootApplicationTests;
import com.kamabizbazti.common.entities.HttpResponse;
import com.kamabizbazti.common.entities.HttpResponseJson;
import com.kamabizbazti.model.entities.dao.GeneralCategory;
import com.kamabizbazti.model.entities.dao.Record;
import com.kamabizbazti.model.entities.external.DatePicker;
import com.kamabizbazti.model.entities.external.ECustomCategory;
import com.kamabizbazti.model.entities.external.ERecord;
import com.kamabizbazti.model.entities.external.EditRecordWrapper;
import com.kamabizbazti.model.exceptions.codes.DataIntegrityErrorCode;
import com.kamabizbazti.model.exceptions.codes.EntitiesErrorCode;
import com.kamabizbazti.security.entities.SignUpWrapper;
import com.kamabizbazti.security.service.JwtAuthenticationResponse;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class UserRestControllerRecordsTest extends KamaBizbaztiBootApplicationTests {

    private String name = "User Records";
    private String email1 = "userrecords1@kamabizbazti.com";
    private String email2 = "userrecords2@kamabizbazti.com";
    private String password = "Password";
    private String token1;
    private String token2;
    private GeneralCategory category;
    private GeneralCategory userCategory;
    private Record record;
    private Record record1;
    private String maxComment = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean m";

    @BeforeClass
    public void setup() {
        SignUpWrapper wrapper = new SignUpWrapper();
        wrapper.setName(name);
        wrapper.setEmail(email1);
        wrapper.setPassword(password);
        JwtAuthenticationResponse jwtToken1 = (JwtAuthenticationResponse) authenticationRestControllerConnectorHelper.createUserPositive(wrapper).getObject();
        token1 = jwtToken1.getToken();
        wrapper.setEmail(email2);
        JwtAuthenticationResponse jwtToken2 = (JwtAuthenticationResponse) authenticationRestControllerConnectorHelper.createUserPositive(wrapper).getObject();
        token2 = jwtToken2.getToken();
        List<GeneralCategory> categoriesUser1 = (List<GeneralCategory>) userRestControllerConnectorHelper.getCategoriesListPositive(token1).getObject();
        category = categoriesUser1.get(0);
        ECustomCategory cat = new ECustomCategory();
        cat.setName("User2 category");
        userCategory = (GeneralCategory) userRestControllerConnectorHelper.addCustomCategoryPositive(cat, token2).getObject();
        ERecord record = new ERecord();
        record.setAmount(10.5);
        record.setCategoryId(category.getCategoryId());
        record.setComment("Comment");
        record.setDate(System.currentTimeMillis());
        this.record1 = (Record) userRestControllerConnectorHelper.addRecordPositive(record, token1).getObject();
    }

    @AfterClass
    public void tearDown() {
        userRepository.delete(userRepository.findByUsername(email1));
        userRepository.delete(userRepository.findByUsername(email2));
    }

    @Test
    public void testAddRecord() {
        ERecord record = new ERecord();
        record.setAmount(10.5);
        record.setCategoryId(category.getCategoryId());
        record.setComment("Comment");
        record.setDate(System.currentTimeMillis());
        Record res = (Record) userRestControllerConnectorHelper.addRecordPositive(record, token1).getObject();
        this.record = res;
        Record r = recordRepository.findOne(1L);
        Assert.assertEquals(res.getAmount(), record.getAmount());
        Assert.assertEquals(res.getComment(), record.getComment());
        Assert.assertEquals(res.getDate(), record.getDate());
        Assert.assertEquals(res.getCategory().getCategoryId() == record.getCategoryId(), true);
    }

    @Test
    public void testAddRecordCyrilicComment() {
        ERecord record = new ERecord();
        record.setAmount(10.5);
        record.setCategoryId(category.getCategoryId());
        record.setComment("Комментарий");
        record.setDate(System.currentTimeMillis());
        Record res = (Record) userRestControllerConnectorHelper.addRecordPositive(record, token1).getObject();
        this.record = res;
        Record r = recordRepository.findOne(1L);
        Assert.assertEquals(res.getAmount(), record.getAmount());
        Assert.assertEquals(res.getComment(), record.getComment());
        Assert.assertEquals(res.getDate(), record.getDate());
        Assert.assertEquals(res.getCategory().getCategoryId() == record.getCategoryId(), true);
    }

    @Test(dependsOnMethods = {"testAddRecord"})
    public void testGetRecordsList() throws IOException {
        DatePicker picker = new DatePicker();
        picker.setStartDate(System.currentTimeMillis() - 1000000);
        picker.setEndDate(System.currentTimeMillis());
        HttpResponseJson response = userRestControllerConnectorHelper.getRecordsListJson(picker, token1);
        Assert.assertTrue(response.getObject().get("totalElements").getAsInt() > 0);
        Record record = (Record) testTools.stringToObject(response.getObject().getAsJsonArray("content").get(0).toString(), Record.class);
        Assert.assertNotNull(record);
    }

    @Test(dependsOnMethods = {"testAddRecord"})
    public void testGetRecordsListEndDateNull() throws IOException {
        DatePicker picker = new DatePicker();
        picker.setStartDate(System.currentTimeMillis());
        picker.setEndDate(null);
        HttpResponseJson response = userRestControllerConnectorHelper.getRecordsListJson(picker, token1);
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "End date can't be null");
    }

    @Test(dependsOnMethods = {"testAddRecord"})
    public void testGetRecordsListStartDateNull() throws IOException {
        DatePicker picker = new DatePicker();
        picker.setStartDate(null);
        picker.setEndDate(System.currentTimeMillis());
        HttpResponseJson response = userRestControllerConnectorHelper.getRecordsListJson(picker, token1);
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Start date can't be null");
    }

    @Test(dependsOnMethods = {"testAddRecord"})
    public void testGetRecordsListEndDateNotProvided() throws IOException {
        DatePicker picker = new DatePicker();
        picker.setStartDate(System.currentTimeMillis());
        HttpResponseJson response = userRestControllerConnectorHelper.getRecordsListJson(picker, token1);
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "End date can't be null");
    }

    @Test(dependsOnMethods = {"testAddRecord"})
    public void testGetRecordsListStartDateNotProvided() throws IOException {
        DatePicker picker = new DatePicker();
        picker.setEndDate(System.currentTimeMillis());
        HttpResponseJson response = userRestControllerConnectorHelper.getRecordsListJson(picker, token1);
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Start date can't be null");
    }

    @Test(dependsOnMethods = {"testAddRecord"})
    public void testGetRecordsListDatePickerNull() {
        HttpResponseJson response = userRestControllerConnectorHelper.getRecordsListJson("{}", token1);
        Assert.assertTrue(response.getObject().get("totalElements").getAsInt() > 0);
        Record record = (Record) testTools.stringToObject(response.getObject().getAsJsonArray("content").get(0).toString(), Record.class);
        Assert.assertNotNull(record);
    }

    @Test(dependsOnMethods = {"testAddRecord"})
    public void testGetRecordsListEndDateBeforeStartDate() throws IOException {
        DatePicker picker = new DatePicker();
        picker.setStartDate(System.currentTimeMillis());
        picker.setEndDate(System.currentTimeMillis() - 100000);
        HttpResponseJson response = userRestControllerConnectorHelper.getRecordsListJson(picker, token1);
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.DATES_ARE_NOT_CHRONOLOGICAL.toString());
        assertEquals(response.getObject().get("message").getAsString(), "End date before start date");
    }

    @Test(dependsOnMethods = {"testAddRecord"})
    public void testEditRecord() throws IOException, InterruptedException {
        ERecord record = new ERecord();
        record.setAmount(11.4);
        record.setCategoryId(category.getCategoryId());
        record.setComment("Comment ffff");
        record.setDate(System.currentTimeMillis());
        EditRecordWrapper wrapper = new EditRecordWrapper();
        wrapper.setRecordId(this.record.getRecordId());
        wrapper.setRecord(record);
        Record res = (Record) userRestControllerConnectorHelper.editRecordPositive(wrapper, token1).getObject();
        Assert.assertEquals(res.getAmount(), record.getAmount());
        Assert.assertEquals(res.getComment(), record.getComment());
        Assert.assertEquals(res.getDate(), record.getDate());
        Assert.assertEquals(res.getCategory().getCategoryId() == record.getCategoryId(), true);
    }

    @Test(dependsOnMethods = {"testAddRecord"})
    public void testEditRecordCreatedByAnotherUser() {
        ERecord record = new ERecord();
        record.setAmount(10.5);
        record.setCategoryId(category.getCategoryId());
        record.setComment("Comment wewe");
        record.setDate(System.currentTimeMillis());
        EditRecordWrapper wrapper = new EditRecordWrapper();
        wrapper.setRecordId(this.record.getRecordId());
        wrapper.setRecord(record);
        HttpResponseJson response = userRestControllerConnectorHelper.editRecordNegative(testTools.objectToJson(wrapper), token2).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), EntitiesErrorCode.RECORD_DOES_NOT_EXIST.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Record Does Not Exist");
    }

    @Test
    public void testEditNotExistingRecord() {
        ERecord record = new ERecord();
        record.setAmount(10.5);
        record.setCategoryId(category.getCategoryId());
        record.setComment("Comment wewe");
        record.setDate(System.currentTimeMillis());
        EditRecordWrapper wrapper = new EditRecordWrapper();
        wrapper.setRecordId(Long.MAX_VALUE);
        wrapper.setRecord(record);
        HttpResponseJson response = userRestControllerConnectorHelper.editRecordNegative(testTools.objectToJson(wrapper), token2).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), EntitiesErrorCode.RECORD_DOES_NOT_EXIST.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Record Does Not Exist");
    }

    @Test(dependsOnMethods = {"testAddRecord"})
    public void testDeleteRecordCreatedByAnotherUser() {
        HttpResponseJson response = userRestControllerConnectorHelper.deleteRecordNegative(String.valueOf(this.record.getRecordId()), token2).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), EntitiesErrorCode.RECORD_DOES_NOT_EXIST.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Record Does Not Exist");
    }

    @Test
    public void testDeleteNotExistingRecord() {
        HttpResponseJson response = userRestControllerConnectorHelper.deleteRecordNegative(String.valueOf(999L), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), EntitiesErrorCode.RECORD_DOES_NOT_EXIST.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Record Does Not Exist");
    }

    @Test
    public void testAddRecordWithoutComment() {
        ERecord record = new ERecord();
        record.setAmount(10.5);
        record.setCategoryId(category.getCategoryId());
        record.setDate(System.currentTimeMillis());
        Record res = (Record) userRestControllerConnectorHelper.addRecordPositive(record, token1).getObject();
        Assert.assertEquals(res.getAmount(), record.getAmount());
        Assert.assertEquals(res.getComment(), record.getComment());
        Assert.assertEquals(res.getDate(), record.getDate());
        Assert.assertEquals(res.getCategory().getCategoryId() == record.getCategoryId(), true);
    }

    @Test
    public void testAddRecordNullComment() {
        ERecord record = new ERecord();
        record.setAmount(10.5);
        record.setCategoryId(category.getCategoryId());
        record.setComment(null);
        record.setDate(System.currentTimeMillis());
        Record res = (Record) userRestControllerConnectorHelper.addRecordPositive(record, token1).getObject();
        Assert.assertEquals(res.getAmount(), record.getAmount());
        Assert.assertEquals(res.getComment(), record.getComment());
        Assert.assertEquals(res.getDate(), record.getDate());
        Assert.assertEquals(res.getCategory().getCategoryId() == record.getCategoryId(), true);
    }

    @Test
    public void testAddRecordMaxComment() {
        ERecord record = new ERecord();
        record.setAmount(10.5);
        record.setCategoryId(category.getCategoryId());
        record.setComment(maxComment);
        record.setDate(System.currentTimeMillis());
        Record res = (Record) userRestControllerConnectorHelper.addRecordPositive(record, token1).getObject();
        Assert.assertEquals(res.getAmount(), record.getAmount());
        Assert.assertEquals(res.getComment(), record.getComment());
        Assert.assertEquals(res.getDate(), record.getDate());
        Assert.assertEquals(res.getCategory().getCategoryId() == record.getCategoryId(), true);
    }

    @Test
    public void testAddRecordLongComment() {
        ERecord record = new ERecord();
        record.setAmount(10.5);
        record.setCategoryId(category.getCategoryId());
        record.setComment(maxComment + "n");
        record.setDate(System.currentTimeMillis());
        HttpResponseJson response = userRestControllerConnectorHelper.addRecordNegative(testTools.objectToJson(record), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Comment length should be less than 100 chars");
    }

    @Test
    public void testAddRecordMissingCategory() {
        JsonObject record = new JsonObject();
        record.addProperty("amount", 10.3);
        record.addProperty("comment", maxComment);
        record.addProperty("date", System.currentTimeMillis());
        HttpResponseJson response = userRestControllerConnectorHelper.addRecordNegative(record.toString(), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Category id can't be blank");
    }

    @Test
    public void testAddRecordNullCategory() {
        JsonObject record = new JsonObject();
        record.addProperty("amount", 10.3);
        record.addProperty("categoryId", (String) null);
        record.addProperty("comment", maxComment);
        record.addProperty("date", System.currentTimeMillis());
        HttpResponseJson response = userRestControllerConnectorHelper.addRecordNegative(testTools.objectToJson(record), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Category id can't be blank");
    }

    @Test
    public void testAddRecordNullAmount() {
        JsonObject record = new JsonObject();
        record.addProperty("amount", (String) null);
        record.addProperty("comment", maxComment);
        record.addProperty("categoryId", category.getCategoryId());
        record.addProperty("date", System.currentTimeMillis());
        HttpResponseJson response = userRestControllerConnectorHelper.addRecordNegative(record.toString(), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Amount can't be null");
    }

    @Test
    public void testAddRecordMissingAmount() {
        JsonObject record = new JsonObject();
        record.addProperty("comment", maxComment);
        record.addProperty("categoryId", category.getCategoryId());
        record.addProperty("date", System.currentTimeMillis());
        HttpResponseJson response = userRestControllerConnectorHelper.addRecordNegative(record.toString(), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Amount can't be null");
    }

    @Test
    public void testAddRecordStringAmount() {
        JsonObject record = new JsonObject();
        record.addProperty("amount", "string");
        record.addProperty("categoryId", category.getCategoryId());
        record.addProperty("comment", maxComment);
        record.addProperty("date", System.currentTimeMillis());
        HttpResponseJson response = userRestControllerConnectorHelper.addRecordNegative(record.toString(), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_REQUEST_ENTITY.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Invalid Request Entity");
    }

    @Test
    public void testAddRecordNullDate() {
        JsonObject record = new JsonObject();
        record.addProperty("amount", 10.3);
        record.addProperty("categoryId", category.getCategoryId());
        record.addProperty("comment", maxComment);
        record.addProperty("date", (Long) null);
        HttpResponseJson response = userRestControllerConnectorHelper.addRecordNegative(testTools.objectToJson(record), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Date can't be null");
    }

    @Test
    public void testAddRecordMissingDate() {
        JsonObject record = new JsonObject();
        record.addProperty("amount", 10.3);
        record.addProperty("categoryId", category.getCategoryId());
        record.addProperty("comment", maxComment);
        HttpResponseJson response = userRestControllerConnectorHelper.addRecordNegative(record.toString(), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Date can't be null");
    }

    @Test
    public void testAddRecordStringDate() {
        JsonObject record = new JsonObject();
        record.addProperty("amount", 10.3);
        record.addProperty("categoryId", category.getCategoryId());
        record.addProperty("comment", maxComment);
        record.addProperty("date", "string");
        HttpResponseJson response = userRestControllerConnectorHelper.addRecordNegative(record.toString(), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_REQUEST_ENTITY.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Invalid Request Entity");
    }

    @Test
    public void testEditRecordWithoutComment() {
        ERecord record = new ERecord();
        record.setAmount(10.5);
        record.setCategoryId(category.getCategoryId());
        record.setDate(System.currentTimeMillis());
        EditRecordWrapper wrapper = new EditRecordWrapper();
        wrapper.setRecordId(this.record.getRecordId());
        wrapper.setRecord(record);
        Record res = (Record) userRestControllerConnectorHelper.editRecordPositive(wrapper, token1).getObject();
        Assert.assertEquals(res.getAmount(), record.getAmount());
        Assert.assertEquals(res.getComment(), record.getComment());
        Assert.assertEquals(res.getDate(), record.getDate());
        Assert.assertTrue(record.getCategoryId().equals(res.getCategory().getCategoryId()));
    }

    @Test
    public void testEditRecordNullComment() {
        ERecord record = new ERecord();
        record.setAmount(10.5);
        record.setCategoryId(category.getCategoryId());
        record.setComment(null);
        record.setDate(System.currentTimeMillis());
        EditRecordWrapper wrapper = new EditRecordWrapper();
        wrapper.setRecordId(this.record.getRecordId());
        wrapper.setRecord(record);
        Record res = (Record) userRestControllerConnectorHelper.editRecordPositive(wrapper, token1).getObject();
        Assert.assertEquals(res.getAmount(), record.getAmount());
        Assert.assertEquals(res.getComment(), record.getComment());
        Assert.assertEquals(res.getDate(), record.getDate());
        Assert.assertTrue(res.getCategory().getCategoryId() == record.getCategoryId());
    }

    @Test
    public void testEditRecordMaxComment() {
        ERecord record = new ERecord();
        record.setAmount(10.5);
        record.setCategoryId(category.getCategoryId());
        record.setComment(maxComment);
        record.setDate(System.currentTimeMillis());
        EditRecordWrapper wrapper = new EditRecordWrapper();
        wrapper.setRecordId(this.record.getRecordId());
        wrapper.setRecord(record);
        Record res = (Record) userRestControllerConnectorHelper.editRecordPositive(wrapper, token1).getObject();
        Assert.assertEquals(res.getAmount(), record.getAmount());
        Assert.assertEquals(res.getComment(), record.getComment());
        Assert.assertEquals(res.getDate(), record.getDate());
        Assert.assertTrue(res.getCategory().getCategoryId() == record.getCategoryId());
    }

    @Test
    public void testEditRecordLongComment() {
        ERecord record = new ERecord();
        record.setAmount(10.5);
        record.setCategoryId(category.getCategoryId());
        record.setComment(maxComment + "n");
        record.setDate(System.currentTimeMillis());
        EditRecordWrapper wrapper = new EditRecordWrapper();
        wrapper.setRecordId(this.record.getRecordId());
        wrapper.setRecord(record);
        HttpResponseJson response = userRestControllerConnectorHelper.editRecordNegative(testTools.objectToJson(wrapper), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Comment length should be less than 100 chars");
    }

    @Test
    public void testEditRecordNullCategory() {
        JsonObject record = new JsonObject();
        record.addProperty("amount", 10.3);
        record.addProperty("categoryId", (String) null);
        record.addProperty("comment", maxComment);
        record.addProperty("date", System.currentTimeMillis());
        JsonObject wrapper = new JsonObject();
        wrapper.addProperty("recordId", this.record.getRecordId());
        wrapper.add("record", record);
        HttpResponseJson response = userRestControllerConnectorHelper.editRecordNegative(testTools.objectToJson(wrapper), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Category id can't be blank");
    }

    @Test
    public void testEditRecordNullAmount() {
        ERecord record = new ERecord();
        record.setAmount(null);
        record.setCategoryId(category.getCategoryId());
        record.setComment(maxComment);
        record.setDate(System.currentTimeMillis());
        EditRecordWrapper wrapper = new EditRecordWrapper();
        wrapper.setRecordId(this.record.getRecordId());
        wrapper.setRecord(record);
        HttpResponseJson response = userRestControllerConnectorHelper.editRecordNegative(testTools.objectToJson(wrapper), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Amount can't be null");
    }

    @Test
    public void testEditRecordStringAmount() {
        JsonObject record = new JsonObject();
        record.addProperty("amount", "string");
        record.addProperty("categoryId", category.getCategoryId());
        record.addProperty("comment", maxComment);
        record.addProperty("date", System.currentTimeMillis());
        JsonObject wrapper = new JsonObject();
        wrapper.addProperty("recordId", this.record.getRecordId());
        wrapper.add("record", record);
        HttpResponseJson response = userRestControllerConnectorHelper.editRecordNegative(testTools.objectToJson(wrapper), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_REQUEST_ENTITY.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Invalid Request Entity");
    }

    @Test
    public void testEditRecordNullDate() {
        ERecord record = new ERecord();
        record.setAmount(10.5);
        record.setCategoryId(category.getCategoryId());
        record.setComment(maxComment);
        record.setDate(null);
        EditRecordWrapper wrapper = new EditRecordWrapper();
        wrapper.setRecordId(this.record.getRecordId());
        wrapper.setRecord(record);
        HttpResponseJson response = userRestControllerConnectorHelper.editRecordNegative(testTools.objectToJson(wrapper), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Date can't be null");
    }

    @Test
    public void testEditRecordStringDate() {
        JsonObject record = new JsonObject();
        record.addProperty("amount", 11.4);
        record.addProperty("categoryId", category.getCategoryId());
        record.addProperty("comment", maxComment);
        record.addProperty("date", "string");
        JsonObject wrapper = new JsonObject();
        wrapper.addProperty("recordId", this.record.getRecordId());
        wrapper.add("record", record);
        HttpResponseJson response = userRestControllerConnectorHelper.editRecordNegative(testTools.objectToJson(wrapper), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_REQUEST_ENTITY.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Invalid Request Entity");
    }

    @Test
    public void testAddRecordWithNotExistingCategory() {
        ERecord record = new ERecord();
        record.setAmount(10.5);
        record.setCategoryId(Long.MAX_VALUE);
        record.setComment("Comment");
        record.setDate(System.currentTimeMillis());
        HttpResponseJson response = userRestControllerConnectorHelper.addRecordNegative(testTools.objectToJson(record), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), EntitiesErrorCode.CATEGORY_DOES_NOT_EXIST.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Category Does Not Exist");
    }

    @Test
    public void testAddRecordWithOnlyCategoryId() {
        ERecord record = new ERecord();
        record.setAmount(10.5);
        record.setCategoryId(category.getCategoryId());
        record.setComment("Comment");
        record.setDate(System.currentTimeMillis());
        Record res = (Record) userRestControllerConnectorHelper.addRecordPositive(record, token1).getObject();
        Assert.assertEquals(res.getAmount(), record.getAmount());
        Assert.assertEquals(res.getComment(), record.getComment());
        Assert.assertEquals(res.getDate(), record.getDate());
        Assert.assertEquals(res.getCategory().getName(), this.category.getName());
    }

    @Test
    public void testAddRecordWithAnotherUserCategory() {
        ECustomCategory category = new ECustomCategory();
        category.setName("Test pps");
        GeneralCategory res = (GeneralCategory) userRestControllerConnectorHelper.addCustomCategoryPositive(category, token2).getObject();
        Assert.assertNotNull(res);
        ERecord record = new ERecord();
        record.setAmount(10.5);
        record.setCategoryId(res.getCategoryId());
        record.setComment("Comment");
        record.setDate(System.currentTimeMillis());
        HttpResponseJson response = userRestControllerConnectorHelper.addRecordNegative(testTools.objectToJson(record), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), EntitiesErrorCode.CATEGORY_DOES_NOT_EXIST.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Category Does Not Exist");
    }

    @Test
    public void testDeleteRecord() {
        HttpResponse response = userRestControllerConnectorHelper.deleteRecordPositive(this.record1.getRecordId(), token1);
        assertEquals(response.getHttpStatusCode(), 200);
    }

    @Test
    public void validateEditWrapperRecordIdNull() {
        ERecord record = new ERecord();
        record.setAmount(10.5);
        record.setCategoryId(category.getCategoryId());
        record.setComment("Comment wewe");
        record.setDate(System.currentTimeMillis());
        EditRecordWrapper wrapper = new EditRecordWrapper();
        wrapper.setRecordId(null);
        wrapper.setRecord(record);
        HttpResponseJson response = userRestControllerConnectorHelper.editRecordNegative(testTools.objectToJson(wrapper), token2).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Record id can't be null");
    }

    @Test
    public void validateEditWrapperWithoutRecordId() {
        ERecord record = new ERecord();
        record.setAmount(10.5);
        record.setCategoryId(category.getCategoryId());
        record.setComment("Comment wewe");
        record.setDate(System.currentTimeMillis());
        EditRecordWrapper wrapper = new EditRecordWrapper();
        wrapper.setRecord(record);
        HttpResponseJson response = userRestControllerConnectorHelper.editRecordNegative(testTools.objectToJson(wrapper), token2).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Record id can't be null");
    }

    @Test
    public void validateEditWrapperRecordNull() {
        EditRecordWrapper wrapper = new EditRecordWrapper();
        wrapper.setRecordId(this.record.getRecordId());
        wrapper.setRecord(null);
        HttpResponseJson response = userRestControllerConnectorHelper.editRecordNegative(testTools.objectToJson(wrapper), token2).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Record can't be null");
    }

    @Test
    public void validateEditWrapperWithoutRecord() {
        EditRecordWrapper wrapper = new EditRecordWrapper();
        wrapper.setRecordId(this.record.getRecordId());
        HttpResponseJson response = userRestControllerConnectorHelper.editRecordNegative(testTools.objectToJson(wrapper), token2).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Record can't be null");
    }
}
