package com.kamabizbazti.restcontroller;

import com.google.gson.JsonObject;
import com.kamabizbazti.KamaBizbaztiBootApplication;
import com.kamabizbazti.common.TestTools;
import com.kamabizbazti.common.entities.HttpResponseJson;
import com.kamabizbazti.common.http.GeneralRestControllerConnectorHelper;
import com.kamabizbazti.config.KamaBizbaztiApplicationConfig;
import com.kamabizbazti.model.entities.dao.ChartSelection;
import com.kamabizbazti.model.entities.external.ChartRequestWrapper;
import com.kamabizbazti.model.entities.external.ChartWrapper;
import com.kamabizbazti.model.enumerations.ChartSelectionIdEnum;
import com.kamabizbazti.model.enumerations.ChartType;
import com.kamabizbazti.model.exceptions.codes.DataIntegrityErrorCode;
import com.kamabizbazti.model.exceptions.codes.EntitiesErrorCode;
import com.kamabizbazti.model.handlers.GeneralRequestHandler;
import com.kamabizbazti.model.handlers.UserRequestHandler;
import com.kamabizbazti.model.repository.ChartSelectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;

@SuppressWarnings({"UnusedDeclaration", "unchecked"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {KamaBizbaztiBootApplication.class, KamaBizbaztiApplicationConfig.class})
@TestPropertySource(locations = "classpath:test.properties")
public class GeneralRestControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private ChartSelectionRepository chartSelectionRepository;

    @Autowired
    private TestTools testTools;

    @LocalServerPort
    private int port;

    private GeneralRestControllerConnectorHelper helper;

    @BeforeClass
    public void setup() {
        helper = new GeneralRestControllerConnectorHelper(port);
    }

    @Test
    public void testGetGeneralChartSelectionsList() throws Exception {
        List<ChartSelection> selectionsDB = chartSelectionRepository.findAllGeneralSelections();
        List<ChartSelection> selectionsHttp = (List<ChartSelection>) helper.getGeneralChartSelectionsListPositive().getObject();
        Assert.assertEquals(selectionsDB.size(), selectionsHttp.size());
        for (ChartSelection c : selectionsDB)
            Assert.assertTrue(selectionsDB.contains(c));
    }

    @Test
    public void testGetUserChartSelectionsList() throws Exception {
        List<ChartSelection> selectionsDB = chartSelectionRepository.findAllUserSelections();
        List<ChartSelection> selectionsHttp = (List<ChartSelection>) helper.getUserChartSelectionsListPositive().getObject();
        Assert.assertEquals(selectionsDB.size(), selectionsHttp.size());
        for (ChartSelection c : selectionsDB)
            Assert.assertTrue(selectionsDB.contains(c));
    }

    @Test
    public void testGetDefaultDataTable() throws Exception {
        ChartWrapper wrapper = (ChartWrapper) helper.getDefaultDataTablePositive().getObject();
        ChartSelection selection = chartSelectionRepository.findOne(GeneralRequestHandler.DEFAULT_CHART_SELECTION);
        Assert.assertNotNull(wrapper);
        Assert.assertNotNull(wrapper.getDataTable());
        Assert.assertEquals(wrapper.getChartType(), selection.getChartType());
        Assert.assertEquals(wrapper.getTitle(), selection.getTitle());
        Assert.assertEquals(wrapper.getChartType(), selection.getChartType());
    }

    @Test
    public void testGetGeneralDataTable() throws Exception {
        List<ChartSelection> selections = (List<ChartSelection>) helper.getGeneralChartSelectionsListPositive().getObject();
        for (ChartSelection c : selections) {
            ChartRequestWrapper requestWrapper = new ChartRequestWrapper();
            requestWrapper.setChartSelection(c);
            ChartWrapper wrapper = (ChartWrapper) helper.getGeneralDataTablePositive(requestWrapper).getObject();
            Assert.assertNotNull(wrapper);
            Assert.assertNotNull(wrapper.getDataTable());
            Assert.assertEquals(c.getChartType(), wrapper.getChartType());
            Assert.assertEquals(c.getTitle(), wrapper.getTitle());
            Assert.assertEquals(c.getChartType(), wrapper.getChartType());
        }
    }

    @Test
    public void testGetGeneralDataTableWrongSelection() throws Exception {
        String wrongSelectionId = "WRONG";
        ChartRequestWrapper requestWrapper = new ChartRequestWrapper();
        requestWrapper.setChartSelection(chartSelectionRepository.findOne(ChartSelectionIdEnum.PREV_MONTH_AVG));
        JsonObject jsonObject = testTools.objectToJsonObject(requestWrapper);
        jsonObject.get("chartSelection").getAsJsonObject().remove("selectionId");
        jsonObject.get("chartSelection").getAsJsonObject().addProperty("selectionId", wrongSelectionId);
        HttpResponseJson response = helper.getGeneralDataTableNegative(jsonObject.toString()).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_REQUEST_ENTITY.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Invalid Request Entity");
    }

    @Test
    public void testGetGeneralDataTableWithSelectionIdOnlyIsValid() throws Exception {
        ChartSelection selectionDB = chartSelectionRepository.findOne(ChartSelectionIdEnum.PREV_MONTH_AVG);
        ChartSelection selection = new ChartSelection();
        selection.setSelectionId(ChartSelectionIdEnum.PREV_MONTH_AVG);
        ChartRequestWrapper requestWrapper = new ChartRequestWrapper();
        requestWrapper.setChartSelection(selection);
        ChartWrapper wrapper = (ChartWrapper) helper.getGeneralDataTablePositive(requestWrapper).getObject();
        Assert.assertNotNull(wrapper);
        Assert.assertNotNull(wrapper.getDataTable());
        Assert.assertEquals(selectionDB.getChartType(), wrapper.getChartType());
        Assert.assertEquals(selectionDB.getTitle(), wrapper.getTitle());
        Assert.assertEquals(selectionDB.getChartType(), wrapper.getChartType());
    }

    @Test
    public void testGetGeneralDataTableWithValidSelectionAndInvalidOtherFields() throws Exception {
        ChartSelection selectionDB = chartSelectionRepository.findOne(ChartSelectionIdEnum.PREV_MONTH_AVG);
        ChartSelection selection = new ChartSelection();
        selection.setSelectionId(ChartSelectionIdEnum.PREV_MONTH_AVG);
        selection.setAuthRequired(true);
        selection.setChartType(ChartType.COLUMNCHART);
        selection.setDatePicker(true);
        selection.setTitle("title");
        ChartRequestWrapper requestWrapper = new ChartRequestWrapper();
        requestWrapper.setChartSelection(selection);
        ChartWrapper wrapper = (ChartWrapper) helper.getGeneralDataTablePositive(requestWrapper).getObject();
        Assert.assertNotNull(wrapper);
        Assert.assertNotNull(wrapper.getDataTable());
        Assert.assertEquals(selectionDB.getChartType(), wrapper.getChartType());
        Assert.assertEquals(selectionDB.getTitle(), wrapper.getTitle());
        Assert.assertEquals(selectionDB.getChartType(), wrapper.getChartType());
    }

    @Test
    public void testGetGeneralDataTableNullSelection() throws Exception {
        ChartRequestWrapper requestWrapper = new ChartRequestWrapper();
        requestWrapper.setChartSelection(null);
        HttpResponseJson response = helper.getGeneralDataTableNegative(testTools.objectToJson(requestWrapper)).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Chart selection can't be blank");
    }

    @Test
    public void testGetGeneralDataTableWithoutSelection() throws Exception {
        HttpResponseJson response = helper.getGeneralDataTableNegative("{}").convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), DataIntegrityErrorCode.INVALID_PARAMETER.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Chart selection can't be blank");
    }

    @Test
    public void testGetGeneralDataTableWithUserSelection() throws Exception {
        ChartSelection selection = chartSelectionRepository.findOne(UserRequestHandler.DEFAULT_CHART_SELECTION);
        ChartRequestWrapper requestWrapper = new ChartRequestWrapper();
        requestWrapper.setChartSelection(selection);
        HttpResponseJson response = helper.getGeneralDataTableNegative(testTools.objectToJson(requestWrapper)).convertToHttpResponseJson();
        assertEquals(response.getHttpStatusCode(), 500);
        assertEquals(response.getObject().get("error").getAsString(), EntitiesErrorCode.UNKNOWN_CHART_SELECTION_ID.toString());
        assertEquals(response.getObject().get("message").getAsString(), "Unknown chart selection ID");
    }
}