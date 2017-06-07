package com.kamabizbazti.model.entities.dao;

import com.kamabizbazti.KamaBizbaztiBootApplicationTests;
import org.springframework.dao.DataIntegrityViolationException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class LanguageTest extends KamaBizbaztiBootApplicationTests {

    private Language language;
    private String languageCode = "GER";
    private String languageCode1 = "DE";
    private Long languageId;

    @AfterClass
    public void tearDown(){
//        languageRepository.deleteByLanguageCode(languageCode);
//        languageRepository.deleteByLanguageCode(languageCode1);
    }

    //TODO
    @Test(expectedExceptions = DataIntegrityViolationException.class, enabled = false)
    public void validateLanguageCodeIsMandatory() {
        try {
            language = new Language();
            language.setLanguageName("German");
            language.setLanguageIcon("german.png");
            languageId = languageRepository.save(language).getId();
        } catch (DataIntegrityViolationException e) {
            throw e;
        } finally {
            System.out.println("FINALLY: " + languageId);
            if(languageId != null)
                languageRepository.delete(languageId);
        }
    }

    //TODO
    @Test(expectedExceptions = DataIntegrityViolationException.class, enabled = false)
    public void validateLanguageCodeCantBeBlank() {
        try {
            language = new Language();
            language.setLanguageCode(" ");
            language.setLanguageName("German");
            language.setLanguageIcon("german.png");
            languageId = languageRepository.save(language).getId();
        } catch (DataIntegrityViolationException e) {
            throw e;
        } finally {
            if(languageId != null)
                languageRepository.delete(languageId);
        }
    }

    //TODO
    @Test(expectedExceptions = DataIntegrityViolationException.class, enabled = false)
    public void validateLanguageNameIsMandatory() {
        try {
            language = new Language();
            language.setLanguageCode(languageCode);
            language.setLanguageIcon("german.png");
            languageId = languageRepository.save(language).getId();
        } catch (DataIntegrityViolationException e) {
            throw e;
        } finally {
            if(languageId != null)
                languageRepository.delete(languageId);
        }
    }

    //TODO
    @Test(expectedExceptions = DataIntegrityViolationException.class, enabled = false)
    public void validateLanguageNameCantBeBlank() {
        try {
            language = new Language();
            language.setLanguageCode(languageCode);
            language.setLanguageName(" ");
            language.setLanguageIcon("german.png");
            languageId = languageRepository.save(language).getId();
        } catch (DataIntegrityViolationException e) {
            throw e;
        } finally {
            if(languageId != null)
                languageRepository.delete(languageId);
        }
    }

    @Test
    public void validateLanguageCanBeSaved() {
        language = new Language();
        language.setLanguageCode(languageCode);
        language.setLanguageName("German");
        language.setLanguageIcon("german.png");
        languageRepository.save(language);
        assertEquals(language, languageRepository.findByLanguageCode(languageCode));
    }

    //TODO
    @Test(enabled = false, expectedExceptions = DataIntegrityViolationException.class, dependsOnMethods = "validateLanguageCanBeSaved")
    public void validateDuplicateLanguageCantBeSaved() {
        languageRepository.save(language);
    }

    @Test
    public void validateLanguageCanBeSavedWithoutIcon() {
        language = new Language();
        language.setLanguageCode(languageCode1);
        language.setLanguageName("German");
        languageRepository.save(language);
        assertEquals(language, languageRepository.findByLanguageCode(languageCode1));
    }
}