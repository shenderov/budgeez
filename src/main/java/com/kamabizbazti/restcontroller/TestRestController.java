package com.kamabizbazti.restcontroller;

import com.kamabizbazti.DataGenerator;
import com.kamabizbazti.model.repository.*;
import com.kamabizbazti.security.repository.AuthorityRepository;
import com.kamabizbazti.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;

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

    @RequestMapping(value = "/insertData", method = RequestMethod.GET)
    @Async
    public void insertData(@RequestParam(value = "generateRecordsForAllUsers", required = false, defaultValue = "false") Boolean generateRecordsForAllUsers,
                                   @RequestParam(value = "generatePurposesForAllUsers", required = false, defaultValue = "false") Boolean generatePurposesForAllUsers,
                                   @RequestParam(value = "generateUsers", required = false, defaultValue = "false") Boolean generateUsers) {
        LinkedList<String> res = new LinkedList<>();
        if (generateUsers) {
            dataGenerator.insertUsers(userRepository, languageRepository, currencyRepository, authorityRepository, passwordEncoder, 2);
            res.add("Users Inserted");
        }
        if (generatePurposesForAllUsers) {
            dataGenerator.insertCustomPurposes(userRepository, generalPurposeRepository, customPurposeRepository);
            res.add("Custom Purposes Inserted");
        }
        if (generateRecordsForAllUsers) {
            dataGenerator.insertRecords(userRepository, recordRepository, customPurposeRepository, 2, 150);
            res.add("Records Inserted");
        }
        if (!generatePurposesForAllUsers && !generateRecordsForAllUsers && !generateUsers) {
            dataGenerator.insertUsers(userRepository, languageRepository, currencyRepository, authorityRepository, passwordEncoder, 2);
            dataGenerator.insertCustomPurposes(userRepository, generalPurposeRepository, customPurposeRepository);
            dataGenerator.insertRecords(userRepository, recordRepository, customPurposeRepository, 2, 150);
            res.add("Users, Custom Purposes and Records Inserted");
        }
        res.add("Data Import Completed");
        for (String str : res)
            System.out.println(str);
    }

    @RequestMapping(value = "/deleteUser", method = RequestMethod.GET)
    public void deleteUser(@RequestParam(value = "uid") Long userId) {
        userRepository.delete(userId);
    }

    @RequestMapping(value = "/deleteAuthority", method = RequestMethod.GET)
    public void deleteAuthority(@RequestParam(value = "id") Long authorityId) {
        authorityRepository.delete(authorityId);
    }

}
