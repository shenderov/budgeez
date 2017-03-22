package com.kamabizbazti.dao;

import com.kamabizbazti.DataGenerator;
import com.kamabizbazti.KamaBizbaztiBootApplication;
import com.kamabizbazti.config.KamaBizbaztiApplicationConfig;
import com.kamabizbazti.model.dao.*;
import com.kamabizbazti.model.enumerations.CategoryType;
import com.kamabizbazti.model.repository.*;
import com.kamabizbazti.security.entities.Authority;
import com.kamabizbazti.security.entities.AuthorityName;
import com.kamabizbazti.model.dao.User;
import com.kamabizbazti.security.repository.AuthorityRepository;
import com.kamabizbazti.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Set;

@SpringBootTest
@ContextConfiguration(classes = {KamaBizbaztiBootApplication.class, KamaBizbaztiApplicationConfig.class})
@TestPropertySource(locations="classpath:test.properties")
public class EntityRelationsValidation extends AbstractTestNGSpringContextTests {

    private static final String currencyCode = "ILS";
    private static final String currencyName = "Sheqel";
    private static final String currencyInUseCode = "USD";
    private static final String currencyInUseName = "Dollar";
    private static final String languageCode = "RUS";
    private static final String languageName = "Rus";
    private static final String languageInUseCode = "ENG";
    private static final String languageInUseName = "Eng";
    private static final String languageToChange = "HEB";
    private static final String currencyToChange = "RUB";
    private static List <User> users;
    private static List <GeneralCategory> customCategories;
    private static List <GeneralCategory> generalCategories;

    @Autowired
    private DataGenerator dataGenerator;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private GeneralCategoryRepository generalCategoryRepository;

    @Autowired
    private CustomCategoryRepository customCategoryRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RecordRepository recordRepository;

    @BeforeClass
    public void generateData() {
        dataGenerator.insertUsers(userRepository, languageRepository, currencyRepository, authorityRepository, passwordEncoder, 2);
        dataGenerator.insertCustomCategories(userRepository, generalCategoryRepository, customCategoryRepository);
        dataGenerator.insertRecords(userRepository, recordRepository, customCategoryRepository, 2, 150);
        users = userRepository.findAll();
    }

    @Test
    public void validateCurrencyCanBeEdited() throws Exception {
        //Currency not in use can be edited
        currencyRepository.setCurrencyName(currencyCode, currencyName);
        currencyRepository.setCurrencyCode(currencyCode, currencyCode + "1");
        Currency edited = currencyRepository.findByCurrencyCode(currencyCode + "1");
        Assert.assertEquals(edited.getCurrencyCode(), currencyCode + "1");
        Assert.assertEquals(edited.getCurrencyName(), currencyName);

        //Currency not in use can be deleted
        currencyRepository.deleteByCurrencyCode(currencyCode + "1");
        Assert.assertNull(currencyRepository.findByCurrencyCode(currencyCode + "1"));

        //Currency in use can be edited
        currencyRepository.setCurrencyName(currencyInUseCode, currencyInUseName);
        currencyRepository.setCurrencyCode(currencyInUseCode, currencyInUseCode + "1");
        Currency edited1 = currencyRepository.findByCurrencyCode(currencyInUseCode + "1");
        Assert.assertEquals(edited1.getCurrencyCode(), currencyInUseCode + "1");
        Assert.assertEquals(edited1.getCurrencyName(), currencyInUseName);

    }

    @Test(expectedExceptions = DataIntegrityViolationException.class, dependsOnMethods = "validateCurrencyCanBeEdited")
    public void validateCurrencyIdInUseCantBeDeleted() throws Exception {
        //Currency not in use can not be deleted
        currencyRepository.deleteByCurrencyCode(currencyInUseCode + "1");
    }

    @Test(dependsOnMethods = "validateCurrencyIdInUseCantBeDeleted")
    public void validateCurrencyIdInUseCantIsNotDeleted() throws Exception {
        Assert.assertNotNull(currencyRepository.findByCurrencyCode(currencyInUseCode + "1"));
    }

    @Test(dependsOnMethods = "validateCurrencyIdInUseCantIsNotDeleted")
    public void validateLanguageCanBeEdited() throws Exception {
        //Language not in use can be edited
        languageRepository.setLanguageName(languageCode, languageName);
        languageRepository.setLanguageCode(languageCode, languageCode + "1");
        Language edited = languageRepository.findByLanguageCode(languageCode + "1");
        Assert.assertEquals(edited.getLanguageCode(), languageCode + "1");
        Assert.assertEquals(edited.getLanguageName(), languageName);

        //Language not in use can deleted
        languageRepository.deleteByLanguageCode(languageCode + "1");
        Assert.assertNull(languageRepository.findByLanguageCode(languageCode + "1"));

        //Language in use can be edited
        languageRepository.setLanguageName(languageInUseCode, languageInUseName);
        languageRepository.setLanguageCode(languageInUseCode, languageInUseCode + "1");
        Language edited1 = languageRepository.findByLanguageCode(languageInUseCode + "1");
        Assert.assertEquals(edited1.getLanguageCode(), languageInUseCode + "1");
        Assert.assertEquals(edited1.getLanguageName(), languageInUseName);
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class, dependsOnMethods = "validateLanguageCanBeEdited")
    public void validateLanguageIdInUseCantBeDeleted() throws Exception {
        //Language not in use can not be deleted
        languageRepository.deleteByLanguageCode(languageInUseCode + "1");
    }

    @Test(dependsOnMethods = "validateLanguageIdInUseCantBeDeleted")
    public void validateLanguageIdInUseIsNotDeleted() throws Exception {
        Assert.assertNotNull(languageRepository.findByLanguageCode(languageInUseCode + "1"));
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class, dependsOnMethods = "validateLanguageIdInUseIsNotDeleted")
    public void validateAuthorityInUseCantBeDeleted() throws Exception {
        //Authority in use can't be deleted
        authorityRepository.delete(authorityRepository.findByName(AuthorityName.ROLE_USER));
        Assert.assertNotNull(authorityRepository.findByName(AuthorityName.ROLE_USER));
    }

    @Test(dependsOnMethods = "validateAuthorityInUseCantBeDeleted")
    public void validateAuthorityInUseIsNotDeleted() throws Exception {
        Assert.assertNotNull(authorityRepository.findByName(AuthorityName.ROLE_USER));
    }

    @Test(dependsOnMethods = "validateAuthorityInUseIsNotDeleted")
    public void validateUserAuthoritiesCanBeUpdated() throws Exception {
        Set<Authority> authorities = users.get(0).getAuthorities();
        Assert.assertEquals(authorities.size(), 1);
        userRepository.grantAuthority(authorityRepository.findByName(AuthorityName.ROLE_TESTER).getId(), users.get(0).getId());
        User user = userRepository.findByUsername(users.get(0).getUsername());
        authorities = user.getAuthorities();
        Assert.assertEquals(authorities.size(), 2);
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class, dependsOnMethods = "validateUserAuthoritiesCanBeUpdated")
    public void validateUserCantHaveDuplicateAuthorities() throws Exception {
        userRepository.grantAuthority(authorityRepository.findByName(AuthorityName.ROLE_TESTER).getId(), users.get(0).getId());
    }

    @Test(dependsOnMethods = "validateUserCantHaveDuplicateAuthorities")
    public void validateUserAuthoritiesCanBeDetached() throws Exception {
        userRepository.detachAuthority(authorityRepository.findByName(AuthorityName.ROLE_TESTER).getId(), users.get(0).getId());
        User user = userRepository.findByUsername(users.get(0).getUsername());
        Set<Authority> authorities = user.getAuthorities();
        Assert.assertEquals(authorities.size(), 1);
    }

    @Test(dependsOnMethods = "validateUserAuthoritiesCanBeDetached")
    public void validateUserLanguageCanBeUpdated() throws Exception {
        userRepository.updateUserLanguage(languageRepository.findByLanguageCode(languageToChange), users.get(0).getId());
        User user = userRepository.findByUsername(users.get(0).getUsername());
        Assert.assertEquals(user.getLanguage().getLanguageCode(), languageRepository.findByLanguageCode(languageToChange).getLanguageCode());
    }

    @Test(dependsOnMethods = "validateUserLanguageCanBeUpdated")
    public void validateUserCurrencyCanBeUpdated() throws Exception {
        userRepository.updateUserCurrency(currencyRepository.findByCurrencyCode(currencyToChange), users.get(0).getId());
        User user = userRepository.findByUsername(users.get(0).getUsername());
        Assert.assertEquals(user.getCurrency().getCurrencyCode(), currencyRepository.findByCurrencyCode(currencyToChange).getCurrencyCode());
    }

    @Test(dependsOnMethods = "validateUserCurrencyCanBeUpdated")
    public void validateRecordCanBeDeleted() throws Exception {
        List <Record> recordsBefore = recordRepository.getRecords(users.get(0).getId(), 1, System.currentTimeMillis());
        recordRepository.deleteRecord(recordsBefore.get(0).getRecordId(), users.get(0).getId());
        List <Record> recordsAfter = recordRepository.getRecords(users.get(0).getId(), 1, System.currentTimeMillis());
        Assert.assertEquals(recordsBefore.size(), recordsAfter.size()+1);
        Assert.assertNull(recordRepository.findOne(recordsBefore.get(0).getRecordId()));
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class, dependsOnMethods = "validateRecordCanBeDeleted")
    public void validateCustomCategoryInUseCantBeDeleted() throws Exception {
        customCategories = customCategoryRepository.findAllCustomCategories(users.get(0).getId());
        customCategoryRepository.delete(customCategories.get(0).getCategoryId());
    }

    @Test(dependsOnMethods = "validateCustomCategoryInUseCantBeDeleted")
    public void validateCustomCategoryWasNotDeleted() throws Exception {
        Assert.assertNotNull(customCategoryRepository.findOne(customCategories.get(0).getCategoryId()));
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class, dependsOnMethods = "validateCustomCategoryWasNotDeleted")
    public void validateGeneralCategoryInUseCantBeDeleted() throws Exception {
        generalCategories = generalCategoryRepository.findAll();
        generalCategoryRepository.delete(generalCategories.get(0).getCategoryId());
    }

    @Test(dependsOnMethods = "validateGeneralCategoryInUseCantBeDeleted")
    public void validateGeneralCategoryWasNotDeleted() throws Exception {
        Assert.assertNotNull(generalCategoryRepository.findOne(generalCategories.get(0).getCategoryId()));
    }

    @Test(dependsOnMethods = "validateGeneralCategoryWasNotDeleted")
    public void validateGeneralCategoryNotInUseCanBeDeleted() throws Exception {
        GeneralCategory category = new GeneralCategory("Test Type", CategoryType.GENERAL);
        long id = generalCategoryRepository.save(category).getCategoryId();
        Assert.assertNotNull(generalCategoryRepository.findOne(id));
        generalCategoryRepository.delete(id);
        Assert.assertNull(generalCategoryRepository.findOne(id));
    }

    @Test(dependsOnMethods = "validateGeneralCategoryNotInUseCanBeDeleted")
    public void validateCustomCategoryNotInUseCanBeDeleted() throws Exception {
        CustomCategory category = new CustomCategory(users.get(0), "User test type");
        long id = customCategoryRepository.save(category).getCategoryId();
        Assert.assertNotNull(customCategoryRepository.findOne(id));
        customCategoryRepository.delete(id);
        Assert.assertNull(customCategoryRepository.findOne(id));
    }

    @Test(dependsOnMethods = "validateCustomCategoryNotInUseCanBeDeleted")
    public void validateUserCanBeDeleted() throws Exception {
        User user1 = userRepository.findOne(users.get(0).getId());
        User user2 = userRepository.findOne(users.get(1).getId());
        Assert.assertNotNull(user1);
        Assert.assertNotNull(user2);
        Assert.assertEquals(user1.getUsername(), users.get(0).getUsername());
        Assert.assertEquals(user2.getUsername(), users.get(1).getUsername());
        List <GeneralCategory> ppsUser1Before = customCategoryRepository.findAllCustomCategories(user1.getId());
        List <GeneralCategory> ppsUser2Before = customCategoryRepository.findAllCustomCategories(user2.getId());
        List <GeneralCategory> generalPpsBefore = generalCategoryRepository.findAll();
        List <Language> languagesBefore = (List <Language>) languageRepository.findAll();
        List <Currency> currenciesBefore = (List <Currency>)currencyRepository.findAll();
        List <Record> recordsUser1Before = recordRepository.getRecords(user1.getId(), 1, System.currentTimeMillis());
        List <Record> recordsUser2Before = recordRepository.getRecords(user2.getId(), 1, System.currentTimeMillis());
        long recordsCountBefore = (recordRepository.count());
        Assert.assertEquals(recordsCountBefore, 599);
        Assert.assertNotNull(ppsUser1Before);
        Assert.assertNotNull(ppsUser2Before);
        Assert.assertEquals(ppsUser1Before.size(), 3);
        Assert.assertEquals(ppsUser2Before.size(), 3);
        Assert.assertNotNull(generalPpsBefore);
        Assert.assertNotNull(languagesBefore);
        Assert.assertEquals(languagesBefore.size(), 2);
        Assert.assertNotNull(currenciesBefore);
        Assert.assertEquals(languagesBefore.size(), 2);
        Assert.assertNotNull(recordsUser1Before);
        Assert.assertEquals(recordsUser1Before.size(), 299);
        Assert.assertNotNull(recordsUser2Before);
        Assert.assertEquals(recordsUser2Before.size(), 300);
        userRepository.delete(user1.getId());
        user1 = userRepository.findOne(users.get(0).getId());
        user2 = userRepository.findOne(users.get(1).getId());
        Assert.assertNull(user1);
        Assert.assertNotNull(user2);
        List <GeneralCategory> ppsUser1After = customCategoryRepository.findAllCustomCategories(users.get(0).getId());
        List <GeneralCategory> ppsUser2After = customCategoryRepository.findAllCustomCategories(user2.getId());
        List <GeneralCategory> generalPpsAfter = generalCategoryRepository.findAll();
        List <Language> languagesAfter = (List <Language>) languageRepository.findAll();
        List <Currency> currenciesAfter = (List <Currency>) currencyRepository.findAll();
        List <Record> recordsUser1After = recordRepository.getRecords(users.get(0).getId(), 1, System.currentTimeMillis());
        List <Record> recordsUser2After = recordRepository.getRecords(user2.getId(), 1, System.currentTimeMillis());
        Assert.assertEquals(customCategoryRepository.findAllCustomCategories(users.get(0).getId()).size(), 0);
        Assert.assertEquals(ppsUser1After.size(), 0);
        Assert.assertNotNull(ppsUser2After);
        Assert.assertNotNull(generalPpsAfter);
        Assert.assertNotNull(languagesAfter);
        Assert.assertNotNull(currenciesAfter);
        Assert.assertEquals(recordRepository.getRecords(users.get(0).getId(), 1, System.currentTimeMillis()).size(), 0);
        Assert.assertEquals(recordsUser1After.size(), 0);
        Assert.assertNotNull(recordsUser2After);
        Assert.assertEquals(ppsUser2Before.size(), ppsUser2After.size());
        Assert.assertEquals(generalPpsBefore.size(), generalPpsAfter.size());
        Assert.assertEquals(languagesBefore.size(), languagesAfter.size());
        Assert.assertEquals(currenciesBefore.size(), currenciesAfter.size());
        Assert.assertEquals(recordsUser2Before.size(), recordsUser2After.size());
        userRepository.deleteAll();
        Assert.assertEquals(userRepository.findAll().size(), 0);
        List <GeneralCategory> customCategories = (List <GeneralCategory>) customCategoryRepository.findAll();
        List <GeneralCategory> generalPpsAfter1 = generalCategoryRepository.findAll();
        List <Language> languagesAfter1 = (List <Language>) languageRepository.findAll();
        List <Currency> currenciesAfter1 = (List <Currency>) currencyRepository.findAll();
        Assert.assertEquals(recordRepository.count(), 0);
        Assert.assertEquals(customCategories.size(), generalPpsAfter1.size());
        Assert.assertNotNull(generalPpsAfter1);
        Assert.assertNotNull(languagesAfter1);
        Assert.assertNotNull(currenciesAfter1);
        Assert.assertEquals(generalPpsBefore.size(), generalPpsAfter1.size());
        Assert.assertEquals(languagesBefore.size(), languagesAfter1.size());
        Assert.assertEquals(currenciesBefore.size(), currenciesAfter1.size());
    }
}
