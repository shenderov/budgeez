package com.kamabizbazti.restcontroller;

import com.kamabizbazti.KamaBizbaztiBootApplication;
import com.kamabizbazti.common.TestTools;
import com.kamabizbazti.common.entities.HttpResponse;
import com.kamabizbazti.common.entities.HttpResponseJson;
import com.kamabizbazti.common.http.AuthenticationRestControllerConnectorHelper;
import com.kamabizbazti.common.http.UserRestControllerConnectorHelper;
import com.kamabizbazti.config.KamaBizbaztiApplicationConfig;
import com.kamabizbazti.model.entities.dao.GeneralCategory;
import com.kamabizbazti.model.entities.dao.Record;
import com.kamabizbazti.model.entities.external.ChartRequestWrapper;
import com.kamabizbazti.model.entities.external.DatePicker;
import com.kamabizbazti.model.entities.external.ERecord;
import com.kamabizbazti.model.enumerations.ChartSelectionIdEnum;
import com.kamabizbazti.model.repository.ChartSelectionRepository;
import com.kamabizbazti.model.repository.GeneralCategoryRepository;
import com.kamabizbazti.security.entities.SignUpWrapper;
import com.kamabizbazti.model.entities.dao.User;
import com.kamabizbazti.security.repository.UserRepository;
import com.kamabizbazti.security.service.JwtAuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@SuppressWarnings({"UnusedDeclaration", "FieldCanBeLocal"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {KamaBizbaztiBootApplication.class, KamaBizbaztiApplicationConfig.class})
@TestPropertySource(locations = "classpath:test.properties")
public class UserRestControllerAuthenticationCheckTest extends AbstractTestNGSpringContextTests {

    @LocalServerPort
    private int port;

    private String name = "User Test Auth";
    private String email = "usertestauth@kamabizbazti.com";
    private String password = "Password";
    private User user;
    private Record record;
    private String token;
    private String expiredToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0QGthbWFiaXpiYXp0aS5jb20iLCJhdWRpZW5jZSI6IndlYiIsImNyZWF0ZWQiOjE0NzkyMzMxNjA0OTcsImV4cCI6MTQ3OTIzMzM0MH0.WdTasVlFcNOzGiRA0gD-f7ExW-DcUWdiudozVmk_xGVU-Y3gzf7bPjNmJe5QALiwneHlsNrqVBnd5pqp6tlNzQ";

    @Autowired
    private ChartSelectionRepository chartSelectionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GeneralCategoryRepository generalCategoryRepository;

    @Autowired
    private TestTools testTools;

    private UserRestControllerConnectorHelper helper;
    private AuthenticationRestControllerConnectorHelper authHelper;

    @BeforeClass
    public void setup() {
        helper = new UserRestControllerConnectorHelper(port);
        authHelper = new AuthenticationRestControllerConnectorHelper(port);
        SignUpWrapper wrapper = new SignUpWrapper();
        wrapper.setName(name);
        wrapper.setEmail(email);
        wrapper.setPassword(password);
        JwtAuthenticationResponse token = (JwtAuthenticationResponse) authHelper.createUserPositive(wrapper).getObject();
        this.token = token.getToken();
        user = userRepository.findByUsername(email);
    }

    @AfterClass
    public void tearDown(){
        userRepository.delete(user.getId());
    }

    @Test
    public void testGetCategoriesList(){
        HttpResponseJson response = helper.getCategoriesListNegative(null).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 401);
        assertEquals(response.getObject().get("error").getAsString(), "Unauthorized");
        assertEquals(response.getObject().get("message").getAsString(), "Unauthorized");
    }

    @Test
    public void testGetUserDefaultDataTable(){
        HttpResponseJson response = helper.getUserDefaultDataTableNegative(null).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 401);
        assertEquals(response.getObject().get("error").getAsString(), "Unauthorized");
        assertEquals(response.getObject().get("message").getAsString(), "Unauthorized");
    }

    @Test
    public void testGetCategoriesListInvalidToken(){
        HttpResponseJson response = helper.getCategoriesListNegative(expiredToken).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 401);
        assertEquals(response.getObject().get("error").getAsString(), "Unauthorized");
        assertEquals(response.getObject().get("message").getAsString(), "Unauthorized");
    }

    @Test
    public void testGetUserDefaultDataTableInvalidToken(){
        HttpResponseJson response = helper.getUserDefaultDataTableNegative(expiredToken).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 401);
        assertEquals(response.getObject().get("error").getAsString(), "Unauthorized");
        assertEquals(response.getObject().get("message").getAsString(), "Unauthorized");
    }

    @Test
    public void testGetUserDataTable(){
        ChartRequestWrapper requestWrapper = new ChartRequestWrapper();
        requestWrapper.setChartSelection(chartSelectionRepository.findOne(ChartSelectionIdEnum.USER_CURRENT_MONTH));
        HttpResponseJson response = helper.getGeneralDataTableNegative(testTools.objectToJson(requestWrapper),null).convertToHttpResponseJson();
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
        HttpResponse httpResponse = helper.addRecordPositive(record, token);
        this.record = (Record) httpResponse.getObject();
        HttpResponseJson response = helper.addRecordNegative(testTools.objectToJson(this.record),null).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 401);
        assertEquals(response.getObject().get("error").getAsString(), "Unauthorized");
        assertEquals(response.getObject().get("message").getAsString(), "Unauthorized");
    }

    @Test
    public void testCustomCategory(){
        GeneralCategory category = new GeneralCategory();
        category.setName("New category");
        HttpResponseJson response = helper.addCustomCategoryNegative(testTools.objectToJson(category),null).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 401);
        assertEquals(response.getObject().get("error").getAsString(), "Unauthorized");
        assertEquals(response.getObject().get("message").getAsString(), "Unauthorized");
    }

    @Test
    public void testGetRecordsList(){
        DatePicker picker = new DatePicker();
        picker.setEndDate(System.currentTimeMillis());
        picker.setStartDate(System.currentTimeMillis()-100000);
        HttpResponseJson response = helper.getRecordsListJson(picker,null);
        assertEquals(response.getHttpStatusCode(), 401);
        assertEquals(response.getObject().get("error").getAsString(), "Unauthorized");
        assertEquals(response.getObject().get("message").getAsString(), "Unauthorized");
    }

    @Test
    public void testDeleteRecord(){
        HttpResponseJson response = helper.deleteRecordNegative("1",null).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 401);
        assertEquals(response.getObject().get("error").getAsString(), "Unauthorized");
        assertEquals(response.getObject().get("message").getAsString(), "Unauthorized");
    }

    @Test(dependsOnMethods = {"testAddRecord"})
    public void testEditRecord(){
        GeneralCategory category = generalCategoryRepository.findAll().get(0);
        record.setCategory(category);
        HttpResponseJson response = helper.editRecordNegative(testTools.objectToJson(record),null).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 401);
        assertEquals(response.getObject().get("error").getAsString(), "Unauthorized");
        assertEquals(response.getObject().get("message").getAsString(), "Unauthorized");
    }
}
