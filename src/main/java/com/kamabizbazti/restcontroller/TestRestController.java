package com.kamabizbazti.restcontroller;

import com.kamabizbazti.DataGenerator;
import com.kamabizbazti.model.entities.GeneralPurpose;
import com.kamabizbazti.model.helpers.DateHelper;
import com.kamabizbazti.model.repository.*;
import com.kamabizbazti.security.repository.AuthorityRepository;
import com.kamabizbazti.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    public String test() {
        dataGenerator.insertChartSelections(chartSelectionRepository);
        dataGenerator.insertLanguages(languageRepository);
        dataGenerator.insertCurrencies(currencyRepository);
        dataGenerator.insertPurposes(generalPurposeRepository);
        dataGenerator.insertAuthorities(authorityRepository);
        dataGenerator.insertUsers(userRepository, languageRepository, currencyRepository, authorityRepository, passwordEncoder, 10);
        dataGenerator.insertCustomPurposes(userRepository, generalPurposeRepository, customPurposeRepository);
        dataGenerator.insertRecords(userRepository, recordRepository, customPurposeRepository, 2, 365);
        System.out.println("Test");
        return "Test";
    }
}
