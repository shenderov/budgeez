package com.kamabizbazti.restcontroller;

import com.kamabizbazti.DataGenerator;
import com.kamabizbazti.model.entities.*;
import com.kamabizbazti.model.handlers.GeneralStatisticsHandler;
import com.kamabizbazti.model.interfaces.IGeneralRequestHandler;
import com.kamabizbazti.model.repository.*;
import com.kamabizbazti.security.repository.AuthorityRepository;
import com.kamabizbazti.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping(path = "general")
@RestController
public class GeneralRestController {

    @Autowired
    DataGenerator dataGenerator;

    @Autowired
    IGeneralRequestHandler handler;

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
    RecordRepository recordRepository;

    @Autowired
    ChartSelectionRepository chartSelectionRepository;

    @Autowired
    GeneralStatisticsHandler generalStatisticsHandler;

    @Autowired
    PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/getGeneralChartSelectionsList", method = RequestMethod.GET)
    public List<ChartSelection> getGeneralChartSelectionsList() {
        return handler.getGeneralChartSelectionsList(chartSelectionRepository);
    }

    @RequestMapping(value = "/getDefaultDataTable", method = RequestMethod.GET)
    public ChartWrapper getDefaultDataTable() throws Exception {
        return handler.getDefaultDataTable(chartSelectionRepository, generalStatisticsHandler, generalPurposeRepository, recordRepository);
    }

    @RequestMapping(value = "/getGeneralDataTable", method = RequestMethod.POST)
    public ChartWrapper getGeneralDataTable(@RequestBody ChartRequestWrapper chartRequestWrapper) throws Exception {
        return handler.getGeneralDatatable(chartRequestWrapper, generalStatisticsHandler, generalPurposeRepository, recordRepository);
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ChartRequestWrapper test1() {
        ChartSelection selection = new ChartSelection(ChartSelectionId.CURRENT_MONTH_AVG, "Current Month Average", ChartType.PIECHART, false, false);
        //ChartRequestWrapper wrapper = new ChartRequestWrapper(selection, 1471727086, 1471803416);
        System.out.println("Test");
        return null;
    }

    @RequestMapping(value = "/insertData", method = RequestMethod.GET)
    public String test() {
//        dataGenerator.insertChartSelections(chartSelectionRepository);
//        dataGenerator.insertLanguages(languageRepository);
//        dataGenerator.insertCurrencies(currencyRepository);
//        dataGenerator.insertPurposes(generalPurposeRepository);
//        dataGenerator.insertAuthorities(authorityRepository);
//        dataGenerator.insertUsers(userRepository, languageRepository, currencyRepository, authorityRepository, passwordEncoder, 10);
//        dataGenerator.insertCustomPurposes(userRepository, generalPurposeRepository, customPurposeRepository);
//        dataGenerator.insertRecords(userRepository, recordRepository, customPurposeRepository, 2, 365);

        System.out.println("Test");
        return "Test";
    }


}
