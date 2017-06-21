package com.kamabizbazti.restcontroller;

import com.google.gson.JsonObject;
import com.kamabizbazti.KamaBizbaztiBootApplicationTests;
import com.kamabizbazti.common.entities.HttpResponseJson;
import com.kamabizbazti.model.entities.dao.GeneralCategory;
import com.kamabizbazti.model.entities.external.ECustomCategory;
import com.kamabizbazti.model.exceptions.codes.DataIntegrityErrorCode;
import com.kamabizbazti.model.exceptions.codes.EntitiesErrorCode;
import com.kamabizbazti.security.entities.SignUpWrapper;
import com.kamabizbazti.security.service.JwtAuthenticationResponse;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class UserRestControllerCategoriesTest extends KamaBizbaztiBootApplicationTests {

    private String name = "User Categories";
    private String email1 = "usercategories1@kamabizbazti.com";
    private String email2 = "usercategories2@kamabizbazti.com";
    private String password = "Password";
    private String token1;
    private String token2;
    private String newCategoryName = "Custom Category";
    private String maxLengthCategoryName = "Lorem ipsum dolor sit amet, co";

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
    }

    @AfterClass
    public void tearDown(){
        userRepository.delete(userRepository.findByUsername(email1));
        userRepository.delete(userRepository.findByUsername(email2));
    }

    @Test
    public void testGetCategoriesList() {
        List <GeneralCategory> categories = (List<GeneralCategory>) userRestControllerConnectorHelper.getCategoriesListPositive(token1).getObject();
        Assert.assertTrue(categories.size() > 0);
    }

    @Test
    public void testAddCategoryUser1() {
        ECustomCategory category = new ECustomCategory();
        category.setName(newCategoryName);
        GeneralCategory res = (GeneralCategory) userRestControllerConnectorHelper.addCustomCategoryPositive(category, token1).getObject();
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getName(), newCategoryName);
        List <GeneralCategory> categoriesUser1 = (List<GeneralCategory>) userRestControllerConnectorHelper.getCategoriesListPositive(token1).getObject();
        List <GeneralCategory> categoriesUser2 = (List<GeneralCategory>) userRestControllerConnectorHelper.getCategoriesListPositive(token2).getObject();
        Assert.assertTrue(listContainsCategoryById(categoriesUser1, res.getCategoryId()));
        Assert.assertFalse(listContainsCategoryById(categoriesUser2, res.getCategoryId()));
    }

    @Test(dependsOnMethods = {"testAddCategoryUser1"})
    public void testAddCategoryDuplicate() {
        ECustomCategory category = new ECustomCategory();
        category.setName(newCategoryName);
        HttpResponseJson response = userRestControllerConnectorHelper.addCustomCategoryNegative(testTools.objectToJson(category), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), EntitiesErrorCode.CUSTOM_CATEGORY_ALREADY_EXIST.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Custom category already exist");
    }

    @Test(dependsOnMethods = {"testAddCategoryUser1"})
    public void testAddCategoryUser2() {
        ECustomCategory category = new ECustomCategory();
        category.setName(newCategoryName);
        GeneralCategory res = (GeneralCategory) userRestControllerConnectorHelper.addCustomCategoryPositive(category, token2).getObject();
        Assert.assertNotNull(res);
        Assert.assertEquals(res.getName(), newCategoryName);
        List <GeneralCategory> categoriesUser1 = (List<GeneralCategory>) userRestControllerConnectorHelper.getCategoriesListPositive(token1).getObject();
        List <GeneralCategory> categoriesUser2 = (List<GeneralCategory>) userRestControllerConnectorHelper.getCategoriesListPositive(token2).getObject();
        Assert.assertFalse(listContainsCategoryById(categoriesUser1, res.getCategoryId()));
        Assert.assertTrue(listContainsCategoryById(categoriesUser2, res.getCategoryId()));
    }

    @Test
    public void testAddCategoriesEmptyRequest() {
        HttpResponseJson response = userRestControllerConnectorHelper.addCustomCategoryNegative("{}", token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Category name can't be blank");
    }

    @Test
    public void testAddCategoryNameNull() {
        JsonObject category = new JsonObject();
        category.addProperty("name", (String) null);
        HttpResponseJson response = userRestControllerConnectorHelper.addCustomCategoryNegative(testTools.objectToJson(category), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Category name can't be blank");
    }

    @Test
    public void testAddCategoryNameBlank() {
        GeneralCategory category = new GeneralCategory();
        category.setName("   ");
        HttpResponseJson response = userRestControllerConnectorHelper.addCustomCategoryNegative(testTools.objectToJson(category), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Category name can't be blank");
    }

    @Test
    public void testAddCategoryLongName() {
        GeneralCategory category = new GeneralCategory();
        category.setName(maxLengthCategoryName + "n");
        HttpResponseJson response = userRestControllerConnectorHelper.addCustomCategoryNegative(testTools.objectToJson(category), token1).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Category name length should be between 1 and 30 chars");
    }

    @Test
    public void testAddCategoryMaxName() {
        ECustomCategory category = new ECustomCategory();
        category.setName(maxLengthCategoryName);
        GeneralCategory res = (GeneralCategory) userRestControllerConnectorHelper.addCustomCategoryPositive(category, token1).getObject();
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
