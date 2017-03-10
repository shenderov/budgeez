package com.kamabizbazti.restcontroller;

import com.google.gson.JsonObject;
import com.kamabizbazti.KamaBizbaztiBootApplication;
import com.kamabizbazti.common.TestTools;
import com.kamabizbazti.common.entities.HttpResponse;
import com.kamabizbazti.common.entities.HttpResponseJson;
import com.kamabizbazti.common.http.AuthenticationRestControllerConnectorHelper;
import com.kamabizbazti.common.http.UserRestControllerConnectorHelper;
import com.kamabizbazti.config.KamaBizbaztiApplicationConfig;
import com.kamabizbazti.model.entities.*;
import com.kamabizbazti.model.exceptions.codes.DataIntegrityErrorCode;
import com.kamabizbazti.model.exceptions.codes.EntitiesErrorCode;
import com.kamabizbazti.security.entities.SignUpWrapper;
import com.kamabizbazti.security.repository.UserRepository;
import com.kamabizbazti.security.service.JwtAuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

import static org.testng.Assert.assertEquals;

@SuppressWarnings({"UnusedDeclaration", "unchecked", "FieldCanBeLocal"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {KamaBizbaztiBootApplication.class, KamaBizbaztiApplicationConfig.class})
@TestPropertySource(locations = "classpath:test.properties")
public class UserRestControllerRecordsTest extends AbstractTestNGSpringContextTests {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestTools testTools;

    private String name = "User Records";
    private String email1 = "userrecords1@kamabizbazti.com";
    private String email2 = "userrecords2@kamabizbazti.com";
    private String password = "Password";
    private String token1;
    private String token2;
    private GeneralPurpose purpose;
    private Record record;
    private String maxComment = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean m";

    private UserRestControllerConnectorHelper helper;
    private AuthenticationRestControllerConnectorHelper authHelper;

    @BeforeClass
    public void setup() {
        helper = new UserRestControllerConnectorHelper(port);
        authHelper = new AuthenticationRestControllerConnectorHelper(port);
        SignUpWrapper wrapper = new SignUpWrapper();
        wrapper.setName(name);
        wrapper.setEmail(email1);
        wrapper.setPassword(password);
        JwtAuthenticationResponse jwtToken1 = (JwtAuthenticationResponse) authHelper.createUserPositive(wrapper).getObject();
        token1 = jwtToken1.getToken();
        wrapper.setEmail(email2);
        JwtAuthenticationResponse jwtToken2 = (JwtAuthenticationResponse) authHelper.createUserPositive(wrapper).getObject();
        token2 = jwtToken2.getToken();
        List <GeneralPurpose> purposesUser1 = (List<GeneralPurpose>) helper.getPurposesListPositive(token1).getObject();
        purpose = purposesUser1.get(0);
    }

    @AfterClass
    public void tearDown(){
        userRepository.delete(userRepository.findByUsername(email1));
        userRepository.delete(userRepository.findByUsername(email2));
    }

    //TODO add test getRecordsList end date before start date
    @Test
    public void testAddRecord() {
        Record record = new Record();
        record.setAmount(10.5);
        record.setPurpose(purpose);
        record.setComment("Comment");
        record.setDate(System.currentTimeMillis());
        Record res = (Record) helper.addRecordPositive(record, token1).getObject();
        this.record = res;
        Assert.assertEquals(res.getAmount(), record.getAmount());
        Assert.assertEquals(res.getComment(), record.getComment());
        Assert.assertEquals(res.getDate(), record.getDate());
        Assert.assertEquals(res.getPurpose().getName(), record.getPurpose().getName());
    }

    @Test(dependsOnMethods = {"testAddRecord"})
    public void testGetRecordsList() throws IOException {
        DatePicker picker = new DatePicker();
        picker.setStartDate(System.currentTimeMillis() - 1000000);
        picker.setEndDate(System.currentTimeMillis());
        HttpResponseJson response = helper.getRecordsListJson(picker, token1);
        Assert.assertTrue(response.getObject().get("totalElements").getAsInt() > 0);
        Record record = (Record) testTools.stringToObject(response.getObject().getAsJsonArray("content").get(0).toString(), Record.class);
        Assert.assertNotNull(record);
    }

    @Test(dependsOnMethods = {"testAddRecord"})
    public void testGetRecordsListEndDateBeforeStartDate() throws IOException {
        DatePicker picker = new DatePicker();
        picker.setStartDate(System.currentTimeMillis());
        picker.setEndDate(System.currentTimeMillis() - 100000);
        HttpResponseJson response = helper.getRecordsListJson(picker, token1);
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.DATES_ARE_NOT_CHRONOLOGICAL.toString());
        assertEquals(response.getObject().get("message").getAsString(), "End date before start date");
    }

    @Test(dependsOnMethods = {"testAddRecord"})
    public void testEditRecord() throws IOException, InterruptedException {
        Record record = this.record;
        record.setAmount(11.4);
        record.setPurpose(purpose);
        record.setComment("Comment ffff");
        record.setDate(System.currentTimeMillis());
        Record res = (Record) helper.editRecordPositive(record, token1).getObject();
        Assert.assertTrue(record.getAmount().equals(res.getAmount()));
        Assert.assertTrue(record.getPurpose().getName().equals(res.getPurpose().getName()));
        Assert.assertTrue(record.getComment().equals(res.getComment()));
        Assert.assertTrue(record.getDate().equals(res.getDate()));
    }

    @Test(dependsOnMethods = {"testAddRecord"})
    public void testEditRecordCreatedByAnotherUser() {
        Record record = this.record;
        record.setAmount(55.5);
        record.setPurpose(purpose);
        record.setComment("Comment wewe");
        record.setDate(System.currentTimeMillis());
        HttpResponseJson response = helper.editRecordNegative(testTools.ObjectToJson(record), token2).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), EntitiesErrorCode.RECORD_DOES_NOT_EXIST.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Record does not exist");
    }

    @Test(dependsOnMethods = {"testEditRecord"})
    public void testEditNotExistingRecord() {
        Record record = this.record;
        record.setRecordId(Long.MAX_VALUE);
        HttpResponseJson response = helper.editRecordNegative(testTools.ObjectToJson(record), token2).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), EntitiesErrorCode.RECORD_DOES_NOT_EXIST.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Record does not exist");
    }

    @Test(dependsOnMethods = {"testAddRecord"})
    public void testDeleteRecordCreatedByAnotherUser() {
        Record record = this.record;
        record.setAmount(55.5);
        record.setPurpose(purpose);
        record.setComment("Comment wewe");
        record.setDate(System.currentTimeMillis());
        HttpResponseJson response = helper.deleteRecordNegative(String.valueOf(record.getRecordId()), token2).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), EntitiesErrorCode.RECORD_DOES_NOT_EXIST.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Record does not exist");
    }

//    Expected :RECORD_DOES_NOT_EXIST
//    Actual   :Internal Server Error
    @Test
    public void testDeleteNotExistingRecord() {
        HttpResponseJson response = helper.deleteRecordNegative(String.valueOf(Long.MAX_VALUE), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), EntitiesErrorCode.RECORD_DOES_NOT_EXIST.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Record does not exist");
    }

    @Test(dependsOnMethods = {"testEditRecord"})
    public void testAddRecordWithoutComment() {
        Record record = new Record();
        record.setAmount(10.5);
        record.setPurpose(purpose);
        record.setDate(System.currentTimeMillis());
        Record res = (Record) helper.addRecordPositive(record, token1).getObject();
        Assert.assertEquals(res.getAmount(), record.getAmount());
        Assert.assertEquals(res.getComment(), record.getComment());
        Assert.assertEquals(res.getDate(), record.getDate());
        Assert.assertEquals(res.getPurpose().getName(), record.getPurpose().getName());
    }

    @Test(dependsOnMethods = {"testEditRecord"})
    public void testAddRecordNullComment() {
        Record record = new Record();
        record.setAmount(10.5);
        record.setPurpose(purpose);
        record.setComment(null);
        record.setDate(System.currentTimeMillis());
        Record res = (Record) helper.addRecordPositive(record, token1).getObject();
        Assert.assertEquals(res.getAmount(), record.getAmount());
        Assert.assertEquals(res.getComment(), record.getComment());
        Assert.assertEquals(res.getDate(), record.getDate());
        Assert.assertEquals(res.getPurpose().getName(), record.getPurpose().getName());
    }

    @Test(dependsOnMethods = {"testEditRecord"})
    public void testAddRecordMaxComment() {
        Record record = new Record();
        record.setAmount(10.5);
        record.setPurpose(purpose);
        record.setComment(maxComment);
        record.setDate(System.currentTimeMillis());
        Record res = (Record) helper.addRecordPositive(record, token1).getObject();
        Assert.assertEquals(res.getAmount(), record.getAmount());
        Assert.assertEquals(res.getComment(), record.getComment());
        Assert.assertEquals(res.getDate(), record.getDate());
        Assert.assertEquals(res.getPurpose().getName(), record.getPurpose().getName());
    }

    @Test
    public void testAddRecordLongComment() {
        Record record = new Record();
        record.setAmount(10.5);
        record.setPurpose(purpose);
        record.setComment(maxComment + "n");
        record.setDate(System.currentTimeMillis());
        HttpResponseJson response = helper.addRecordNegative(testTools.ObjectToJson(record), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Comment length should be less than 100 chars");
    }

    @Test
    public void testAddRecordMissingPurpose() {
        JsonObject record = new JsonObject();
        record.addProperty("amount", 10.3);
        record.addProperty("comment", maxComment);
        record.addProperty("date", System.currentTimeMillis());
        HttpResponseJson response = helper.addRecordNegative(record.toString(), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Purpose can't be blank");
    }

    @Test
    public void testAddRecordNullPurpose() {
        JsonObject record = new JsonObject();
        record.addProperty("amount", 10.3);
        record.addProperty("purpose", (String) null);
        record.addProperty("comment", maxComment);
        record.addProperty("date", System.currentTimeMillis());
        HttpResponseJson response = helper.addRecordNegative(testTools.ObjectToJson(record), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Purpose can't be blank");
    }

    @Test
    public void testAddRecordNullAmount() {
        JsonObject record = new JsonObject();
        record.addProperty("amount", (String) null);
        record.addProperty("comment", maxComment);
        record.addProperty("purpose", testTools.ObjectToJson(purpose));
        record.addProperty("date", System.currentTimeMillis());
        HttpResponseJson response = helper.addRecordNegative(record.toString(), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Amount can't be blank");
    }

    @Test
    public void testAddRecordMissingAmount() {
        JsonObject record = new JsonObject();
        record.addProperty("comment", maxComment);
        record.addProperty("purpose", testTools.ObjectToJson(purpose));
        record.addProperty("date", System.currentTimeMillis());
        HttpResponseJson response = helper.addRecordNegative(record.toString(), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Amount can't be blank");
    }

    @Test
    public void testAddRecordStringAmount() {
        JsonObject record = new JsonObject();
        record.addProperty("amount", "string");
        record.addProperty("purpose", testTools.ObjectToJson(purpose));
        record.addProperty("comment", maxComment);
        record.addProperty("date", System.currentTimeMillis());
        HttpResponseJson response = helper.addRecordNegative(record.toString(), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_DATA_TYPE.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Invalid Data Type");
    }

    @Test
    public void testAddRecordNullDate() {
        JsonObject record = new JsonObject();
        record.addProperty("amount", 10.3);
        record.addProperty("purpose", testTools.ObjectToJson(purpose));
        record.addProperty("comment", maxComment);
        record.addProperty("date", (Long) null);
        HttpResponseJson response = helper.addRecordNegative(testTools.ObjectToJson(record), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Date can't be blank");
    }

    @Test
    public void testAddRecordMissingDate() {
        JsonObject record = new JsonObject();
        record.addProperty("amount", 10.3);
        record.addProperty("purpose", testTools.ObjectToJson(purpose));
        record.addProperty("comment", maxComment);
        HttpResponseJson response = helper.addRecordNegative(record.toString(), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Date can't be blank");
    }

    @Test
    public void testAddRecordStringDate() {
        JsonObject record = new JsonObject();
        record.addProperty("amount", 10.3);
        record.addProperty("purpose", testTools.ObjectToJson(purpose));
        record.addProperty("comment", maxComment);
        record.addProperty("date", "string");
        HttpResponseJson response = helper.addRecordNegative(record.toString(), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_DATA_TYPE.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Invalid Data Type");
    }

    @Test(dependsOnMethods = {"testEditRecord"})
    public void testEditRecordWithoutComment() {
        Record record = this.record;
        record.setAmount(10.5);
        record.setPurpose(purpose);
        record.setDate(System.currentTimeMillis());
        Record res = (Record) helper.editRecordPositive(record, token1).getObject();
        Assert.assertEquals(res.getAmount(), record.getAmount());
        Assert.assertEquals(res.getComment(), record.getComment());
        Assert.assertEquals(res.getDate(), record.getDate());
        Assert.assertEquals(res.getPurpose().getName(), record.getPurpose().getName());
    }

    @Test(dependsOnMethods = {"testEditRecord"})
    public void testEditRecordNullComment() {
        Record record = this.record;
        record.setAmount(10.5);
        record.setPurpose(purpose);
        record.setComment(null);
        record.setDate(System.currentTimeMillis());
        Record res = (Record) helper.editRecordPositive(record, token1).getObject();
        Assert.assertEquals(res.getAmount(), record.getAmount());
        Assert.assertEquals(res.getComment(), record.getComment());
        Assert.assertEquals(res.getDate(), record.getDate());
        Assert.assertEquals(res.getPurpose().getName(), record.getPurpose().getName());
    }

    @Test(dependsOnMethods = {"testEditRecord"})
    public void testEditRecordMaxComment() {
        Record record = this.record;
        record.setAmount(10.5);
        record.setPurpose(purpose);
        record.setComment(maxComment);
        record.setDate(System.currentTimeMillis());
        Record res = (Record) helper.editRecordPositive(record, token1).getObject();
        Assert.assertEquals(res.getAmount(), record.getAmount());
        Assert.assertEquals(res.getComment(), record.getComment());
        Assert.assertEquals(res.getDate(), record.getDate());
        Assert.assertEquals(res.getPurpose().getName(), record.getPurpose().getName());
    }

    //TODO add proper exception
    @Test
    public void testEditRecordLongComment() {
        Record record = this.record;
        record.setAmount(10.5);
        record.setPurpose(purpose);
        record.setComment(maxComment + "n");
        record.setDate(System.currentTimeMillis());
        HttpResponseJson response = helper.editRecordNegative(testTools.ObjectToJson(record), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Comment length should be less than 100 chars");
    }

    @Test
    public void testEditRecordNullPurpose() {
        Record record = this.record;
        record.setPurpose(purpose);
        record.setAmount(11.4);
        record.setComment(maxComment);
        record.setDate(System.currentTimeMillis());
        JsonObject jsonObject = testTools.objectToJsonObject(record);
        jsonObject.remove("purpose");
        jsonObject.addProperty("purpose", (String) null);
        HttpResponseJson response = helper.editRecordNegative(testTools.ObjectToJson(record), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Purpose can't be blank");
    }

    @Test
    public void testEditRecordNullAmount() {
        Record record = this.record;
        record.setAmount(null);
        record.setPurpose(purpose);
        record.setComment(maxComment);
        record.setDate(System.currentTimeMillis());
        HttpResponseJson response = helper.editRecordNegative(testTools.ObjectToJson(record), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Amount can't be blank");
    }

    @Test
    public void testEditRecordStringAmount() {
        Record record = this.record;
        record.setPurpose(purpose);
        record.setAmount(11.4);
        record.setComment(maxComment);
        record.setDate(System.currentTimeMillis());
        JsonObject jsonObject = testTools.objectToJsonObject(record);
        jsonObject.remove("amount");
        jsonObject.addProperty("amount", "string");
        HttpResponseJson response = helper.editRecordNegative(jsonObject.toString(), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_DATA_TYPE.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Invalid Data Type");
    }

    @Test
    public void testEditRecordNullDate() {
        Record record = this.record;
        record.setAmount(10.4);
        record.setPurpose(purpose);
        record.setComment(maxComment);
        record.setDate(null);
        HttpResponseJson response = helper.editRecordNegative(testTools.ObjectToJson(record), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Date can't be blank");
    }

    @Test
    public void testEditRecordStringDate() {
        Record record = this.record;
        record.setPurpose(purpose);
        record.setAmount(11.4);
        record.setComment(maxComment);
        record.setDate(System.currentTimeMillis());
        JsonObject jsonObject = testTools.objectToJsonObject(record);
        jsonObject.remove("date");
        jsonObject.addProperty("date", "string");
        HttpResponseJson response = helper.editRecordNegative(jsonObject.toString(), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_DATA_TYPE.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Invalid Data Type");
    }

    @Test
    public void testAddRecordWithNotExistingPurpose() {
        GeneralPurpose purpose = new GeneralPurpose();
        purpose.setName("Not existing");
        purpose.setType(PurposeType.GENERAL);
        purpose.setuId(Long.MAX_VALUE);
        Record record = new Record();
        record.setAmount(10.5);
        record.setPurpose(purpose);
        record.setComment(maxComment);
        record.setDate(System.currentTimeMillis());
        HttpResponseJson response = helper.addRecordNegative(testTools.ObjectToJson(record), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), EntitiesErrorCode.PURPOSE_DOES_NOT_EXIST.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Purpose Does Not Exist");
    }

    @Test(dependsOnMethods = {"testEditRecord"})
    public void testAddRecordWithOnlyPurposeId() {
        GeneralPurpose purpose = new GeneralPurpose();
        purpose.setuId(this.purpose.getuId());
        Record record = new Record();
        record.setAmount(10.5);
        record.setPurpose(purpose);
        record.setComment("Comment");
        record.setDate(System.currentTimeMillis());
        Record res = (Record) helper.addRecordPositive(record, token1).getObject();
        Assert.assertEquals(res.getAmount(), record.getAmount());
        Assert.assertEquals(res.getComment(), record.getComment());
        Assert.assertEquals(res.getDate(), record.getDate());
        Assert.assertEquals(res.getPurpose().getName(), this.purpose.getName());
    }

    @Test(dependsOnMethods = {"testEditRecord"})
    public void testAddRecordWithValidIdAndInvalidParameters() {
        GeneralPurpose purpose = new GeneralPurpose();
        purpose.setuId(this.purpose.getuId());
        purpose.setName("Bla-bla-bla");
        purpose.setType(PurposeType.CUSTOM);
        Record record = new Record();
        record.setAmount(10.5);
        record.setPurpose(purpose);
        record.setComment("Comment");
        record.setDate(System.currentTimeMillis());
        Record res = (Record) helper.addRecordPositive(record, token1).getObject();
        Assert.assertEquals(res.getAmount(), record.getAmount());
        Assert.assertEquals(res.getComment(), record.getComment());
        Assert.assertEquals(res.getDate(), record.getDate());
        Assert.assertEquals(res.getPurpose().getName(), this.purpose.getName());
    }

    @Test(dependsOnMethods = {"testEditRecord"})
    public void testAddRecordWithAnotherUserPurpose() {
        CustomPurpose purpose = new CustomPurpose();
        purpose.setName("Test pps");
        GeneralPurpose res = (GeneralPurpose) helper.addCustomPurposePositive(purpose, token2).getObject();
        Assert.assertNotNull(res);
        Record record = new Record();
        record.setAmount(10.5);
        record.setPurpose(res);
        record.setComment("Comment");
        record.setDate(System.currentTimeMillis());
        HttpResponseJson response = helper.addRecordNegative(testTools.ObjectToJson(record), token1).convertToHttpResponseJson();
        System.out.println(response.getObject().toString());
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), EntitiesErrorCode.PURPOSE_DOES_NOT_EXIST.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Purpose Does Not Exist");
    }

    @Test(dependsOnMethods = {"testEditRecord"})
    public void testDeleteRecord() {
        Record record = this.record;
        record.setAmount(55.5);
        record.setPurpose(purpose);
        record.setComment("Comment wewe");
        record.setDate(System.currentTimeMillis());
        HttpResponse response = helper.deleteRecordPositive(record.getRecordId(), token1);
        assertEquals(response.getHttpStatusCode(), 200);
    }
}
