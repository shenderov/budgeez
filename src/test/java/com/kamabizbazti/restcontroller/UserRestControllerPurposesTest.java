package com.kamabizbazti.restcontroller;

import com.google.gson.JsonObject;
import com.kamabizbazti.KamaBizbaztiBootApplication;
import com.kamabizbazti.common.TestTools;
import com.kamabizbazti.common.entities.HttpResponseJson;
import com.kamabizbazti.common.http.AuthenticationRestControllerConnectorHelper;
import com.kamabizbazti.common.http.UserRestControllerConnectorHelper;
import com.kamabizbazti.config.KamaBizbaztiApplicationConfig;
import com.kamabizbazti.model.entities.CustomPurpose;
import com.kamabizbazti.model.entities.GeneralPurpose;
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

import java.util.List;

import static org.testng.Assert.assertEquals;

@SuppressWarnings({"UnusedDeclaration", "unchecked", "FieldCanBeLocal"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {KamaBizbaztiBootApplication.class, KamaBizbaztiApplicationConfig.class})
@TestPropertySource(locations = "classpath:test.properties")
public class UserRestControllerPurposesTest extends AbstractTestNGSpringContextTests {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestTools testTools;

    private String name = "User Purposes";
    private String email1 = "userpurposes1@kamabizbazti.com";
    private String email2 = "userpurposes2@kamabizbazti.com";
    private String password = "Password";
    private String token1;
    private String token2;
    private String newPurposeName = "Custom Purpose";
    private String maxLengthPurposeName = "Lorem ipsum dolor sit amet, co";

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
    }

    @AfterClass
    public void tearDown(){
        userRepository.delete(userRepository.findByUsername(email1));
        userRepository.delete(userRepository.findByUsername(email2));
    }

    @Test
    public void testGetPurposesList() {
        List <GeneralPurpose> purposes = (List<GeneralPurpose>) helper.getPurposesListPositive(token1).getObject();
        Assert.assertTrue(purposes.size() > 0);
    }

    @Test
    public void testAddPurposeUser1() {
        CustomPurpose purpose = new CustomPurpose();
        purpose.setName(newPurposeName);
        GeneralPurpose res = (GeneralPurpose) helper.addCustomPurposePositive(purpose, token1).getObject();
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getName(), newPurposeName);
        List <GeneralPurpose> purposesUser1 = (List<GeneralPurpose>) helper.getPurposesListPositive(token1).getObject();
        List <GeneralPurpose> purposesUser2 = (List<GeneralPurpose>) helper.getPurposesListPositive(token2).getObject();
        Assert.assertTrue(listContainsPurposeById(purposesUser1, res.getPurposeId()));
        Assert.assertFalse(listContainsPurposeById(purposesUser2, res.getPurposeId()));
    }

    @Test(dependsOnMethods = {"testAddPurposeUser1"})
    public void testAddPurposeDuplicate() {
        CustomPurpose purpose = new CustomPurpose();
        purpose.setName(newPurposeName);
        HttpResponseJson response = helper.addCustomPurposeNegative(testTools.ObjectToJson(purpose), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), EntitiesErrorCode.CUSTOM_PURPOSE_ALREADY_EXIST.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Custom Purpose Already Exist");
    }

    @Test(dependsOnMethods = {"testAddPurposeUser1"})
    public void testAddPurposeUser2() {
        CustomPurpose purpose = new CustomPurpose();
        purpose.setName(newPurposeName);
        GeneralPurpose res = (GeneralPurpose) helper.addCustomPurposePositive(purpose, token2).getObject();
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getName(), newPurposeName);
        List <GeneralPurpose> purposesUser1 = (List<GeneralPurpose>) helper.getPurposesListPositive(token1).getObject();
        List <GeneralPurpose> purposesUser2 = (List<GeneralPurpose>) helper.getPurposesListPositive(token2).getObject();
        Assert.assertFalse(listContainsPurposeById(purposesUser1, res.getPurposeId()));
        Assert.assertTrue(listContainsPurposeById(purposesUser2, res.getPurposeId()));
    }

    //TODO Handle exception
    @Test
    public void testAddPurposeEmptyRequest() {
        HttpResponseJson response = helper.addCustomPurposeNegative("{}", token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_REQUEST_ENTITY.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Invalid Request Entity");
    }

    //TODO Handle exception
    @Test
    public void testAddPurposeNameNull() {
        JsonObject purpose = new JsonObject();
        purpose.addProperty("name", (String) null);
        HttpResponseJson response = helper.addCustomPurposeNegative(testTools.ObjectToJson(purpose), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Name can't be blank");
    }

    //TODO Handle exception
    @Test
    public void testAddPurposeNameBlank() {
        CustomPurpose purpose = new CustomPurpose();
        purpose.setName("   ");
        HttpResponseJson response = helper.addCustomPurposeNegative(testTools.ObjectToJson(purpose), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Name can't be blank");
    }

    //TODO Handle exception
    @Test
    public void testAddPurposeLongName() {
        CustomPurpose purpose = new CustomPurpose();
        purpose.setName(maxLengthPurposeName + "n");
        HttpResponseJson response = helper.addCustomPurposeNegative(testTools.ObjectToJson(purpose), token1).convertToHttpResponseJson();
        System.out.println(response.getObject().toString());
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Name length should be between 1 and 30 chars");
    }

    @Test
    public void testAddPurposeMaxName() {
        CustomPurpose purpose = new CustomPurpose();
        purpose.setName(maxLengthPurposeName);
        GeneralPurpose res = (GeneralPurpose) helper.addCustomPurposePositive(purpose, token1).getObject();
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getName(), maxLengthPurposeName);
    }

    private boolean listContainsPurposeById(List <GeneralPurpose> list, long id){
        for (GeneralPurpose pps : list){
            if(pps.getPurposeId() == id){
                return true;
            }
        }
        return false;
    }
}
