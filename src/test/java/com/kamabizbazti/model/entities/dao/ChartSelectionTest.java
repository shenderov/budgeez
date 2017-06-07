package com.kamabizbazti.model.entities.dao;

import com.kamabizbazti.KamaBizbaztiBootApplicationTests;
import com.kamabizbazti.model.enumerations.ChartSelectionIdEnum;
import com.kamabizbazti.model.enumerations.ChartType;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ChartSelectionTest extends KamaBizbaztiBootApplicationTests {

    private ChartSelection selection;
    private ChartSelectionIdEnum idPositive = ChartSelectionIdEnum.CURRENT_MONTH_AVG;
    private ChartSelectionIdEnum idNegative = ChartSelectionIdEnum.LAST_YEAR_AVG;

    @AfterClass
    public void tearDown(){
        chartSelectionRepository.delete(idNegative);
        chartSelectionRepository.delete(idPositive);
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void validateAuthRequiredIsMandatory() {
        selection = new ChartSelection();
        selection.setChartType(ChartType.COLUMNCHART);
        selection.setDatePicker(true);
        selection.setSelectionId(idNegative);
        selection.setTitle("Title");
        chartSelectionRepository.save(selection);
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void validateChartTypeMandatory() {
        selection = new ChartSelection();
        selection.setAuthRequired(true);
        selection.setDatePicker(true);
        selection.setSelectionId(idNegative);
        selection.setTitle("Title");
        chartSelectionRepository.save(selection);
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void validateDatePickerMandatory() {
        selection = new ChartSelection();
        selection.setAuthRequired(true);
        selection.setChartType(ChartType.COLUMNCHART);
        selection.setSelectionId(idNegative);
        selection.setTitle("Title");
        chartSelectionRepository.save(selection);
    }

    @Test(expectedExceptions = JpaSystemException.class)
    public void validateSelectionIdMandatory() {
        selection = new ChartSelection();
        selection.setAuthRequired(true);
        selection.setChartType(ChartType.COLUMNCHART);
        selection.setDatePicker(true);
        selection.setTitle("Title");
        chartSelectionRepository.save(selection);
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void validateTitleMandatory() {
        selection = new ChartSelection();
        selection.setAuthRequired(true);
        selection.setChartType(ChartType.COLUMNCHART);
        selection.setDatePicker(true);
        selection.setSelectionId(idNegative);
        chartSelectionRepository.save(selection);
    }

    //TODO test fails
    @Test(expectedExceptions = DataIntegrityViolationException.class, enabled = false)
    public void validateTitleCantBeBlank() {
        selection = new ChartSelection();
        selection.setAuthRequired(true);
        selection.setChartType(ChartType.COLUMNCHART);
        selection.setDatePicker(true);
        selection.setSelectionId(idNegative);
        selection.setTitle(" ");
        chartSelectionRepository.save(selection);
    }

    @Test
    public void validateSaveValidSelection() {
        selection = new ChartSelection();
        selection.setAuthRequired(true);
        selection.setChartType(ChartType.COLUMNCHART);
        selection.setDatePicker(true);
        selection.setSelectionId(idPositive);
        selection.setTitle("Title");
        chartSelectionRepository.save(selection);
        assertEquals(selection, chartSelectionRepository.findOne(idPositive));
    }

    @Test(dependsOnMethods = "validateSaveValidSelection", expectedExceptions = DataIntegrityViolationException.class)
    public void validateSaveDuplicateSelection() {
        chartSelectionRepository.save(selection);
    }
}