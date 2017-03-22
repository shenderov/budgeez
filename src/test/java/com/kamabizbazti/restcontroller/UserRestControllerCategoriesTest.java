package com.kamabizbazti.restcontroller;

import com.google.gson.JsonObject;
import com.kamabizbazti.KamaBizbaztiBootApplication;
import com.kamabizbazti.common.TestTools;
import com.kamabizbazti.common.entities.HttpResponseJson;
import com.kamabizbazti.common.http.AuthenticationRestControllerConnectorHelper;
import com.kamabizbazti.common.http.UserRestControllerConnectorHelper;
import com.kamabizbazti.config.KamaBizbaztiApplicationConfig;
import com.kamabizbazti.model.dao.GeneralCategory;
import com.kamabizbazti.model.entities.ECustomCategory;
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
public class UserRestControllerCategoriesTest extends AbstractTestNGSpringContextTests {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestTools testTools;

    private String name = "User Categories";
    private String email1 = "usercategories1@kamabizbazti.com";
    private String email2 = "usercategories2@kamabizbazti.com";
    private String password = "Password";
    private String token1;
    private String token2;
    private String newCategoryName = "Custom Category";
    private String maxLengthCategoryName = "Lorem ipsum dolor sit amet, co";

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
    public void testGetCategoriesList() {
        List <GeneralCategory> categories = (List<GeneralCategory>) helper.getCategoriesListPositive(token1).getObject();
        Assert.assertTrue(categories.size() > 0);
    }

    @Test
    public void testAddCategoryUser1() {
        ECustomCategory category = new ECustomCategory();
        category.setName(newCategoryName);
        GeneralCategory res = (GeneralCategory) helper.addCustomCategoryPositive(category, token1).getObject();
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getName(), newCategoryName);
        List <GeneralCategory> categoriesUser1 = (List<GeneralCategory>) helper.getCategoriesListPositive(token1).getObject();
        List <GeneralCategory> categoriesUser2 = (List<GeneralCategory>) helper.getCategoriesListPositive(token2).getObject();
        Assert.assertTrue(listContainsCategoryById(categoriesUser1, res.getCategoryId()));
        Assert.assertFalse(listContainsCategoryById(categoriesUser2, res.getCategoryId()));
    }

    @Test(dependsOnMethods = {"testAddCategoryUser1"})
    public void testAddCategoryDuplicate() {
        ECustomCategory category = new ECustomCategory();
        category.setName(newCategoryName);
        HttpResponseJson response = helper.addCustomCategoryNegative(testTools.objectToJson(category), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), EntitiesErrorCode.CUSTOM_CATEGORY_ALREADY_EXIST.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Custom category already exist");
    }

    @Test(dependsOnMethods = {"testAddCategoryUser1"})
    public void testAddCategoryUser2() {
        ECustomCategory category = new ECustomCategory();
        category.setName(newCategoryName);
        GeneralCategory res = (GeneralCategory) helper.addCustomCategoryPositive(category, token2).getObject();
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getName(), newCategoryName);
        List <GeneralCategory> categoriesUser1 = (List<GeneralCategory>) helper.getCategoriesListPositive(token1).getObject();
        List <GeneralCategory> categoriesUser2 = (List<GeneralCategory>) helper.getCategoriesListPositive(token2).getObject();
        Assert.assertFalse(listContainsCategoryById(categoriesUser1, res.getCategoryId()));
        Assert.assertTrue(listContainsCategoryById(categoriesUser2, res.getCategoryId()));
    }

    @Test
    public void testAddCategoriesEmptyRequest() {
        HttpResponseJson response = helper.addCustomCategoryNegative("{}", token1).convertToHttpResponseJson();
        System.out.println(response.getObject().toString());
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Category name can't be blank");
    }

    @Test
    public void testAddCategoryNameNull() {
        JsonObject category = new JsonObject();
        category.addProperty("name", (String) null);
        HttpResponseJson response = helper.addCustomCategoryNegative(testTools.objectToJson(category), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Category name can't be blank");
    }

    @Test
    public void testAddCategoryNameBlank() {
        GeneralCategory category = new GeneralCategory();
        category.setName("   ");
        HttpResponseJson response = helper.addCustomCategoryNegative(testTools.objectToJson(category), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Category name can't be blank");
    }

    @Test
    public void testAddCategoryLongName() {
        GeneralCategory category = new GeneralCategory();
        category.setName(maxLengthCategoryName + "n");
        HttpResponseJson response = helper.addCustomCategoryNegative(testTools.objectToJson(category), token1).convertToHttpResponseJson();
        System.out.println(response.getObject().toString());
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Category name length should be between 1 and 30 chars");
    }

    @Test
    public void testAddCategoryMaxName() {
        ECustomCategory category = new ECustomCategory();
        category.setName(maxLengthCategoryName);
        GeneralCategory res = (GeneralCategory) helper.addCustomCategoryPositive(category, token1).getObject();
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getName(), maxLengthCategoryName);
    }

    private boolean listContainsCategoryById(List <GeneralCategory> list, long id){
        for (GeneralCategory categories : list){
            if(categories.getCategoryId() == id){
                return true;
            }
        }
        return false;
    }
}
