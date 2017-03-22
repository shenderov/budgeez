package com.kamabizbazti.restcontroller;

import com.google.gson.JsonObject;
import com.kamabizbazti.KamaBizbaztiBootApplication;
import com.kamabizbazti.common.TestTools;
import com.kamabizbazti.common.entities.HttpResponseJson;
import com.kamabizbazti.common.http.AuthenticationRestControllerConnectorHelper;
import com.kamabizbazti.common.http.GeneralRestControllerConnectorHelper;
import com.kamabizbazti.common.http.UserRestControllerConnectorHelper;
import com.kamabizbazti.config.KamaBizbaztiApplicationConfig;
import com.kamabizbazti.model.dao.ChartSelection;
import com.kamabizbazti.model.entities.*;
import com.kamabizbazti.model.enumerations.ChartSelectionIdEnum;
import com.kamabizbazti.model.enumerations.ChartType;
import com.kamabizbazti.model.exceptions.codes.DataIntegrityErrorCode;
import com.kamabizbazti.model.exceptions.codes.EntitiesErrorCode;
import com.kamabizbazti.model.handlers.GeneralRequestHandler;
import com.kamabizbazti.model.handlers.UserRequestHandler;
import com.kamabizbazti.model.interfaces.IDateHelper;
import com.kamabizbazti.model.repository.ChartSelectionRepository;
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

import java.util.List;

import static org.testng.Assert.assertEquals;

@SuppressWarnings({"UnusedDeclaration", "unchecked", "FieldCanBeLocal"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {KamaBizbaztiBootApplication.class, KamaBizbaztiApplicationConfig.class})
@TestPropertySource(locations = "classpath:test.properties")
public class UserRestControllerGetUserDatatableTest extends AbstractTestNGSpringContextTests {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChartSelectionRepository chartSelectionRepository;

    @Autowired
    private TestTools testTools;

    @Autowired
    private IDateHelper dateHelper;

    private String name = "User Datatable";
    private String email1 = "userdatatable1@kamabizbazti.com";
    private String password = "Password";
    private String token1;

    private UserRestControllerConnectorHelper helper;
    private AuthenticationRestControllerConnectorHelper authHelper;
    private GeneralRestControllerConnectorHelper generalHelper;

    @BeforeClass
    public void setup() {
        helper = new UserRestControllerConnectorHelper(port);
        authHelper = new AuthenticationRestControllerConnectorHelper(port);
        generalHelper = new GeneralRestControllerConnectorHelper(port);
        SignUpWrapper wrapper = new SignUpWrapper();
        wrapper.setName(name);
        wrapper.setEmail(email1);
        wrapper.setPassword(password);
        JwtAuthenticationResponse jwtToken1 = (JwtAuthenticationResponse) authHelper.createUserPositive(wrapper).getObject();
        token1 = jwtToken1.getToken();
    }

    @AfterClass
    public void tearDown(){
        userRepository.delete(userRepository.findByUsername(email1));
    }

    @Test
    public void testGetUserDefaultDataTable() throws Exception {
        ChartWrapper wrapper = (ChartWrapper) helper.getUserDefaultDataTablePositive(token1).getObject();
        ChartSelection selection = chartSelectionRepository.findOne(UserRequestHandler.DEFAULT_CHART_SELECTION);
        Assert.assertNotNull(wrapper);
        Assert.assertNotNull(wrapper.getDataTable());
        Assert.assertEquals(wrapper.getChartType(), selection.getChartType());
        Assert.assertEquals(wrapper.getTitle(), selection.getTitle());
        Assert.assertEquals(wrapper.getChartType(), selection.getChartType());
    }

    @Test
    public void testGetUserGeneralDataTable() throws Exception {
        List<ChartSelection> selections = (List<ChartSelection>) generalHelper.getUserChartSelectionsListPositive().getObject();
        DatePicker datePicker = new DatePicker();
        datePicker.setEndDate(dateHelper.getLastDayOfCurrentMonth());
        datePicker.setStartDate(dateHelper.getFirstDayOfCurrentMonth());
        for (ChartSelection c : selections) {
            ChartRequestWrapper requestWrapper = new ChartRequestWrapper();
            requestWrapper.setChartSelection(c);
            if(c.isDatePicker())
                requestWrapper.setDatePicker(datePicker);
            ChartWrapper wrapper = (ChartWrapper) helper.getGeneralDataTablePositive(requestWrapper, token1).getObject();
            Assert.assertNotNull(wrapper);
            Assert.assertNotNull(wrapper.getDataTable());
            Assert.assertEquals(c.getChartType(), wrapper.getChartType());
            Assert.assertEquals(c.getTitle(), wrapper.getTitle());
            Assert.assertEquals(c.getChartType(), wrapper.getChartType());
        }
    }

    @Test
    public void testGetUserGeneralDataTableWrongSelection() throws Exception {
        String wrongSelectionId = "WRONG";
        ChartRequestWrapper requestWrapper = new ChartRequestWrapper();
        requestWrapper.setChartSelection(chartSelectionRepository.findOne(ChartSelectionIdEnum.USER_CURRENT_MONTH));
        JsonObject jsonObject = testTools.objectToJsonObject(requestWrapper);
        jsonObject.get("chartSelection").getAsJsonObject().remove("selectionId");
        jsonObject.get("chartSelection").getAsJsonObject().addProperty("selectionId", wrongSelectionId);
        HttpResponseJson response = helper.getGeneralDataTableNegative(jsonObject.toString(), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_REQUEST_ENTITY.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Invalid Request Entity");
    }

    @Test
    public void testGetGeneralDataTableWithSelectionIdOnlyIsValid() throws Exception {
        ChartSelection selectionDB = chartSelectionRepository.findOne(ChartSelectionIdEnum.USER_CURRENT_MONTH);
        ChartSelection selection = new ChartSelection();
        selection.setSelectionId(ChartSelectionIdEnum.USER_CURRENT_MONTH);
        ChartRequestWrapper requestWrapper = new ChartRequestWrapper();
        requestWrapper.setChartSelection(selection);
        ChartWrapper wrapper = (ChartWrapper) helper.getGeneralDataTablePositive(requestWrapper, token1).getObject();
        Assert.assertNotNull(wrapper);
        Assert.assertNotNull(wrapper.getDataTable());
        Assert.assertEquals(selectionDB.getChartType(), wrapper.getChartType());
        Assert.assertEquals(selectionDB.getTitle(), wrapper.getTitle());
        Assert.assertEquals(selectionDB.getChartType(), wrapper.getChartType());
    }

    @Test
    public void testGetGeneralDataTableWithValidSelectionAndInvalidOtherFields() throws Exception {
        ChartSelection selectionDB = chartSelectionRepository.findOne(ChartSelectionIdEnum.USER_CURRENT_MONTH);
        ChartSelection selection = new ChartSelection();
        selection.setSelectionId(ChartSelectionIdEnum.USER_CURRENT_MONTH);
        selection.setAuthRequired(true);
        selection.setChartType(ChartType.COLUMNCHART);
        selection.setDatePicker(true);
        selection.setTitle("title");
        ChartRequestWrapper requestWrapper = new ChartRequestWrapper();
        requestWrapper.setChartSelection(selection);
        ChartWrapper wrapper = (ChartWrapper) helper.getGeneralDataTablePositive(requestWrapper, token1).getObject();
        Assert.assertNotNull(wrapper);
        Assert.assertNotNull(wrapper.getDataTable());
        Assert.assertEquals(selectionDB.getChartType(), wrapper.getChartType());
        Assert.assertEquals(selectionDB.getTitle(), wrapper.getTitle());
        Assert.assertEquals(selectionDB.getChartType(), wrapper.getChartType());
    }

    @Test
    public void testGetUserGeneralDataTableNullSelection() throws Exception {
        ChartRequestWrapper requestWrapper = new ChartRequestWrapper();
        requestWrapper.setChartSelection(null);
        HttpResponseJson response = helper.getGeneralDataTableNegative(testTools.objectToJson(requestWrapper), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Chart selection can't be blank");
    }

    @Test
    public void testDataTableRequiresDatePickerWithDatePickerNull() throws Exception {
        ChartSelection selectionDB = chartSelectionRepository.findOne(ChartSelectionIdEnum.USER_CUSTOM_PERIOD_AVG);
        ChartRequestWrapper requestWrapper = new ChartRequestWrapper();
        requestWrapper.setChartSelection(selectionDB);
        HttpResponseJson response = helper.getGeneralDataTableNegative(testTools.objectToJson(requestWrapper), token1).convertToHttpResponseJson();
        System.out.println(response.getObject().toString());
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Date picker can't be blank");
    }

    @Test
    public void testDataTableRequiresDatePickerWithEndDateBeforeStartDate() throws Exception {
        ChartSelection selectionDB = chartSelectionRepository.findOne(ChartSelectionIdEnum.USER_CUSTOM_PERIOD_AVG);
        DatePicker picker = new DatePicker();
        picker.setEndDate(System.currentTimeMillis()-100000);
        picker.setStartDate(System.currentTimeMillis());
        ChartRequestWrapper requestWrapper = new ChartRequestWrapper();
        requestWrapper.setChartSelection(selectionDB);
        requestWrapper.setDatePicker(picker);
        HttpResponseJson response = helper.getGeneralDataTableNegative(testTools.objectToJson(requestWrapper), token1).convertToHttpResponseJson();
        System.out.println(response.getObject().toString());
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.DATES_ARE_NOT_CHRONOLOGICAL.toString());
        assertEquals(response.getObject().get("message").getAsString(), "End date before start date");
    }

    @Test
    public void testGetUserGeneralDataTableWithoutSelection() throws Exception {
        HttpResponseJson response = helper.getGeneralDataTableNegative("{}", token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Chart selection can't be blank");
    }

    @Test
    public void testGetUserDataTableWithGeneralSelection() throws Exception {
        ChartSelection selection = chartSelectionRepository.findOne(GeneralRequestHandler.DEFAULT_CHART_SELECTION);
        ChartRequestWrapper requestWrapper = new ChartRequestWrapper();
        requestWrapper.setChartSelection(selection);
        HttpResponseJson response = helper.getGeneralDataTableNegative(testTools.objectToJson(requestWrapper), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), EntitiesErrorCode.UNKNOWN_CHART_SELECTION_ID.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Unknown chart selection ID");
    }

    @Test
    public void testGetUserGeneralDataTableOnlyStartDate() throws Exception {
        ChartSelection selection = chartSelectionRepository.findOne(ChartSelectionIdEnum.USER_CUSTOM_PERIOD_AVG);
        DatePicker datePicker = new DatePicker();
        datePicker.setStartDate(dateHelper.getFirstDayOfCurrentMonth());
        ChartRequestWrapper requestWrapper = new ChartRequestWrapper();
        requestWrapper.setChartSelection(selection);
        requestWrapper.setDatePicker(datePicker);
        HttpResponseJson response = helper.getGeneralDataTableNegative(testTools.objectToJson(requestWrapper), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "End date can't be null");
    }

    @Test
    public void testGetUserGeneralDataTableOnlyEndDate() throws Exception {
        ChartSelection selection = chartSelectionRepository.findOne(ChartSelectionIdEnum.USER_CUSTOM_PERIOD_AVG);
        DatePicker datePicker = new DatePicker();
        datePicker.setEndDate(dateHelper.getLastDayOfCurrentMonth());
        ChartRequestWrapper requestWrapper = new ChartRequestWrapper();
        requestWrapper.setChartSelection(selection);
        requestWrapper.setDatePicker(datePicker);
        HttpResponseJson response = helper.getGeneralDataTableNegative(testTools.objectToJson(requestWrapper), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Start date can't be null");
    }

    @Test
    public void testGetUserGeneralDataTableStartDateString() throws Exception {
        ChartSelection selection = chartSelectionRepository.findOne(ChartSelectionIdEnum.USER_CUSTOM_PERIOD_AVG);
        JsonObject datePicker = new JsonObject();
        datePicker.addProperty("startDate", "string");
        datePicker.addProperty("endDate", dateHelper.getLastDayOfCurrentMonth());
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("chartSelection", testTools.objectToJson(selection));
        jsonObject.addProperty("datePicker", datePicker.toString());
        HttpResponseJson response = helper.getGeneralDataTableNegative(jsonObject.toString(), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_REQUEST_ENTITY.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Invalid Request Entity");
    }

    @Test
    public void testGetUserGeneralDataTableOnlyStartDateNegative() throws Exception {
        ChartSelection selection = chartSelectionRepository.findOne(ChartSelectionIdEnum.USER_CUSTOM_PERIOD_AVG);
        DatePicker datePicker = new DatePicker();
        datePicker.setEndDate(dateHelper.getLastDayOfCurrentMonth());
        datePicker.setStartDate(-100000L);
        ChartRequestWrapper requestWrapper = new ChartRequestWrapper();
        requestWrapper.setChartSelection(selection);
        requestWrapper.setDatePicker(datePicker);
        HttpResponseJson response = helper.getGeneralDataTableNegative(testTools.objectToJson(requestWrapper), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Start date should be positive number");
    }

}
