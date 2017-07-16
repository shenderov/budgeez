package com.budgeez.model.entities.dao;

import com.budgeez.BudgeezBootApplicationTests;
import org.springframework.dao.DataIntegrityViolationException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class CurrencyTest extends BudgeezBootApplicationTests {

    private Currency currency;
    private String currencyCode = "EUR";
    private String currencyCode1 = "EU";
    private Long currencyId;

    @AfterClass
    public void tearDown(){
        currencyRepository.deleteByCurrencyCode(currencyCode);
        currencyRepository.deleteByCurrencyCode(currencyCode1);
        currencyRepository.deleteByCurrencyCode(" ");
        if(currencyId != null)
            currencyRepository.delete(currencyId);
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void validateCurrencyCodeIsMandatory() {
        currency = new Currency();
        currency.setCurrencyName("Euro");
        currency.setCurrencySymbol('$');
        currencyId = currencyRepository.save(currency).getId();
    }

    //TODO
    @Test
    public void validateCurrencyCodeCantBeBlank() {
        currency = new Currency();
        currency.setCurrencyCode(" ");
        currency.setCurrencyName("Euro");
        currency.setCurrencySymbol('$');
        //currencyId = currencyRepository.save(currency).getId();
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void validateCurrencyNameIsMandatory() {
        currency = new Currency();
        currency.setCurrencyCode(currencyCode);
        currency.setCurrencySymbol('$');
        currencyRepository.save(currency);
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void validateCurrencyNameCantBeBlank() {
        currency = new Currency();
        currency.setCurrencyCode(currencyCode);
        currency.setCurrencyName(" ");
        currency.setCurrencySymbol('$');
        currencyRepository.save(currency);
    }

    @Test
    public void validateCurrencyCanBeSaved() {
        currency = new Currency();
        currency.setCurrencyCode(currencyCode);
        currency.setCurrencyName("Euro");
        currency.setCurrencySymbol('$');
        currencyRepository.save(currency);
        assertEquals(currency, currencyRepository.findByCurrencyCode(currencyCode));
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class, dependsOnMethods = "validateCurrencyCanBeSaved")
    public void validateDuplicateCurrencyCantBeSaved() {
        currencyRepository.save(currency);
    }

    @Test
    public void validateCurrencyCanBeSavedWithoutSymbol() {
        currency = new Currency();
        currency.setCurrencyCode(currencyCode1);
        currency.setCurrencyName("Euro");
        currencyRepository.save(currency);
        assertEquals(currency, currencyRepository.findByCurrencyCode(currencyCode1));
    }
}