package com.kamabizbazti.restcontroller;

import com.google.common.collect.Iterables;
import com.kamabizbazti.DataGenerator;
import com.kamabizbazti.model.entities.ChartSelection;
import com.kamabizbazti.model.entities.Currency;
import com.kamabizbazti.model.entities.GeneralPurpose;
import com.kamabizbazti.model.entities.Language;
import com.kamabizbazti.model.helpers.DateHelper;
import com.kamabizbazti.model.repository.*;
import com.kamabizbazti.security.entities.Authority;
import com.kamabizbazti.security.repository.AuthorityRepository;
import com.kamabizbazti.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RequestMapping(path = "test")
@RestController
@CrossOrigin(origins = "*")
public class TestRestController {

    @Autowired
    DataGenerator dataGenerator;

    @Autowired
    ChartSelectionRepository chartSelectionRepository;

    @Autowired
    LanguageRepository languageRepository;

    @Autowired
    CurrencyRepository currencyRepository;

    @Autowired
    GeneralPurposeRepository generalPurposeRepository;

    @Autowired
    CustomPurposeRepository customPurposeRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RecordRepository recordRepository;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test1() {
        //ChartSelection selection = new ChartSelection(ChartSelectionId.CURRENT_MONTH_AVG, "Current Month Average", ChartType.PIECHART, false, false);
        //ChartRequestWrapper wrapper = new ChartRequestWrapper(selection, 1471727086, 1471803416);
        DateHelper helper = new DateHelper();
        System.out.println(helper.getFirstDayOfPreviousMonth());
        System.out.println(helper.getLastDayOfPreviousMonth());
        System.out.println();
        System.out.println("Test");
        // Boolean res = recordRepository.isPurposeHaveRecords(3, helper.getFirstDayOfPreviousMonth(), helper.getLastDayOfPreviousMonth());
        List<GeneralPurpose> ppss = generalPurposeRepository.getAllActualGeneralPurposes(helper.getFirstDayOfPreviousMonth(), helper.getLastDayOfPreviousMonth());
        for (GeneralPurpose pps : ppss)
            System.out.println(pps.getName());
        return "Test";
    }

    @RequestMapping(value = "/insertData", method = RequestMethod.GET)
    public List<String> insertData(@RequestParam(value = "generateRecordsForAllUsers", required = false, defaultValue = "false") Boolean generateRecordsForAllUsers,
                                   @RequestParam(value = "generatePurposesForAllUsers", required = false, defaultValue = "false") Boolean generatePurposesForAllUsers,
                                   @RequestParam(value = "generateUsers", required = false, defaultValue = "false") Boolean generateUsers) {
        LinkedList<String> res = new LinkedList<>();
        if (generateUsers) {
            dataGenerator.insertUsers(userRepository, languageRepository, currencyRepository, authorityRepository, passwordEncoder, 10);
            res.add("Users Inserted");
        }
        if (generatePurposesForAllUsers) {
            dataGenerator.insertCustomPurposes(userRepository, generalPurposeRepository, customPurposeRepository);
            res.add("Custom Purposes Inserted");
        }
        if (generateRecordsForAllUsers) {
            dataGenerator.insertRecords(userRepository, recordRepository, customPurposeRepository, 2, 365);
            res.add("Records Inserted");
        }
        if (!generatePurposesForAllUsers && !generateRecordsForAllUsers && !generateUsers) {
            dataGenerator.insertUsers(userRepository, languageRepository, currencyRepository, authorityRepository, passwordEncoder, 10);
            dataGenerator.insertCustomPurposes(userRepository, generalPurposeRepository, customPurposeRepository);
            dataGenerator.insertRecords(userRepository, recordRepository, customPurposeRepository, 2, 365);
            res.add("Users, Custom Purposes and Records Inserted");
        }
        res.add("Data Import Completed");
        for (String str : res)
            System.out.println(str);
        return res;
    }

    @RequestMapping(value = "/insertInitiateData", method = RequestMethod.GET)
    public List<String> insertInitiateData() {
        LinkedList<String> res = new LinkedList<>();
        Iterable<Language> languages = languageRepository.findAll();
        Iterable<Currency> currencies = currencyRepository.findAll();
        Iterable<ChartSelection> chartSelections = chartSelectionRepository.findAll();
        Iterable<GeneralPurpose> purposes = generalPurposeRepository.findAll();
        Iterable<Authority> authorities = authorityRepository.findAll();
        if (Iterables.size(languages) == 0) {
            dataGenerator.insertLanguages(languageRepository);
            res.add("Languages Inserted");
        }
        if (Iterables.size(currencies) == 0) {
            dataGenerator.insertCurrencies(currencyRepository);
            res.add("Currencies Inserted");
        }
        if (Iterables.size(chartSelections) == 0) {
            dataGenerator.insertChartSelections(chartSelectionRepository);
            res.add("Chart Selections Inserted");
        }
        if (Iterables.size(purposes) == 0) {
            dataGenerator.insertPurposes(generalPurposeRepository);
            res.add("General Purposes Inserted");
        }
        if (Iterables.size(authorities) == 0) {
            dataGenerator.insertAuthorities(authorityRepository);
            res.add("Authorities Inserted");
        }
        res.add("Initiate Data Import Completed");
        for (String str : res)
            System.out.println(str);
        return res;
    }
}
