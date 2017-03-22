package com.kamabizbazti;

import com.kamabizbazti.model.dao.*;
import com.kamabizbazti.model.dao.Currency;
import com.kamabizbazti.model.enumerations.ChartSelectionIdEnum;
import com.kamabizbazti.model.enumerations.ChartType;
import com.kamabizbazti.model.repository.*;
import com.kamabizbazti.security.entities.Authority;
import com.kamabizbazti.security.entities.AuthorityName;
import com.kamabizbazti.model.dao.User;
import com.kamabizbazti.security.repository.AuthorityRepository;
import com.kamabizbazti.security.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;


public class DataGenerator {
    private static final String NAMES_DATABASE = "src/test/resources/15000NamesDatabase.txt";

    public void insertLanguages(LanguageRepository languageRepository) {
        long start = System.currentTimeMillis();
        Language lang = new Language("RUS", "Russian");
        Language lang1 = new Language("ENG", "English");
        Language lang2 = new Language("HEB", "Hebrew");
        languageRepository.save(lang);
        languageRepository.save(lang1);
        languageRepository.save(lang2);
        System.out.println(String.format("Languages loaded in %dms", System.currentTimeMillis() - start));
    }

    public void insertCurrencies(CurrencyRepository currencyRepository) {
        long start = System.currentTimeMillis();
        Currency currency = new Currency("RUB", "Russian Ruble");
        Currency currency1 = new Currency("USD", "US Dollar");
        Currency currency2 = new Currency("ILS", "Israeli New Sheqel");
        currencyRepository.save(currency);
        currencyRepository.save(currency1);
        currencyRepository.save(currency2);
        System.out.println(String.format("Currencies loaded in %dms", System.currentTimeMillis() - start));
    }

    public void insertCategories(GeneralCategoryRepository generalCategoryRepository) {
        long start = System.currentTimeMillis();
        generalCategoryRepository.save(new GeneralCategory("Food"));
        generalCategoryRepository.save(new GeneralCategory("Car"));
        generalCategoryRepository.save(new GeneralCategory("Gas"));
        generalCategoryRepository.save(new GeneralCategory("Beer"));
        generalCategoryRepository.save(new GeneralCategory("Bills"));
        generalCategoryRepository.save(new GeneralCategory("Internet"));
        generalCategoryRepository.save(new GeneralCategory("Travel"));
        generalCategoryRepository.save(new GeneralCategory("Vodka"));
        generalCategoryRepository.save(new GeneralCategory("Transportation"));
        generalCategoryRepository.save(new GeneralCategory("Pubs"));
        System.out.println(String.format("GeneralCategories loaded in %dms", System.currentTimeMillis() - start));
    }


    public void insertAuthorities(AuthorityRepository authorityRepository) {
        long start = System.currentTimeMillis();
        Authority authorityAdmin = new Authority();
        Authority authorityUser = new Authority();
        authorityAdmin.setName(AuthorityName.ROLE_ADMIN);
        authorityUser.setName(AuthorityName.ROLE_USER);
        authorityRepository.save(authorityAdmin);
        authorityRepository.save(authorityUser);
        System.out.println(String.format("Authorities loaded in %dms", System.currentTimeMillis() - start));
    }

    public void insertUsers(UserRepository userRepository, LanguageRepository languageRepository, CurrencyRepository currencyRepository, AuthorityRepository authorityRepository, PasswordEncoder passwordEncoder, int count) {
        long start = System.currentTimeMillis();
        System.out.println(new File(NAMES_DATABASE).getAbsoluteFile());
        String[] lines = fileReader(NAMES_DATABASE);
        Set<String> set = new HashSet<>();
        Language language = languageRepository.findByLanguageCode("ENG");
        Currency currency = currencyRepository.findByCurrencyCode("USD");
        String pass = passwordEncoder.encode("123456");
        Set<Authority> authorities = new TreeSet<>();
        authorities.add(authorityRepository.findOne(Long.parseLong("2")));
        for (String str : lines)
            set.add(str);
        String[] db = set.toArray(new String[set.size()]);
        System.out.println(String.format("Users data prepared in %dms", System.currentTimeMillis() - start));
        for (int i = 0; i < count; i++) {
            String[] details = db[i].split("	");
            User user = new User(details[0].toLowerCase() + "." + details[1].toLowerCase() + "@domain.com", pass, details[0] + " " + details[1]);
            user.setLanguage(language);
            user.setCurrency(currency);
            user.setAuthorities(authorities);
            userRepository.save(user);
            System.out.println(String.format("User ID %d saved", user.getId()));
        }
        System.out.println(String.format("Users loaded in %dms", System.currentTimeMillis() - start));
    }

    public void insertRecords(UserRepository userRepository, RecordRepository recordRepository, CustomCategoryRepository customCategoryRepository, int recordsPerDay, int days) {
        long start = System.currentTimeMillis();
        Iterable<User> users = userRepository.findAll();
        long date;
        System.out.println(String.format("Records data prepared in %dms", System.currentTimeMillis() - start));
        for (User u : users) {
            System.out.println("Loading records for User ID: " + u.getId());
            List<GeneralCategory> categories = customCategoryRepository.findAllUserSpecified(u.getId());
            date = System.currentTimeMillis();
            for (int i = 0; i < days; i++) {
                for (int j = 0; j < recordsPerDay; j++) {
                    Record record = new Record(u, getRandomCategory(categories), getRandomAmount(0.5, 199.9), date);
                    recordRepository.save(record);
                }
                date = date - TimeUnit.DAYS.toMillis(1);
            }
        }
        System.out.println(String.format("Records loaded in %dms", System.currentTimeMillis() - start));
    }

    public void insertCustomCategories(UserRepository userRepository, GeneralCategoryRepository generalCategoryRepository, CustomCategoryRepository customCategoryRepository) {
        long start = System.currentTimeMillis();
        Iterable<User> users = userRepository.findAll();
        for (User u : users) {
            for (int i = 0; i < 3; i++) {
                customCategoryRepository.save(new CustomCategory(u, u.getName() + " " + (i + 1)));
            }
        }
        System.out.println(String.format("CustomCategories loaded in %dms", System.currentTimeMillis() - start));
    }

    public void insertChartSelections(ChartSelectionRepository chartSelectionRepository) {
        long start = System.currentTimeMillis();
        ChartSelection selection1 = new ChartSelection(ChartSelectionIdEnum.CURRENT_MONTH_AVG, "Current Month Average", ChartType.PIECHART, false, false);
        ChartSelection selection2 = new ChartSelection(ChartSelectionIdEnum.PREV_MONTH_AVG, "Previous Month Average", ChartType.PIECHART, false, false);
        ChartSelection selection3 = new ChartSelection(ChartSelectionIdEnum.PREV_THREE_MONTH_AVG, "Previous 3 Month Average", ChartType.PIECHART, false, false);
        ChartSelection selection4 = new ChartSelection(ChartSelectionIdEnum.LAST_YEAR_AVG, "Last Year Average", ChartType.PIECHART, false, false);
        ChartSelection selection5 = new ChartSelection(ChartSelectionIdEnum.LAST_THREE_MONTH_AVG_DETAILED, "Last 3 Month Average Detailed", ChartType.COLUMNCHART, false, false);
        ChartSelection selection6 = new ChartSelection(ChartSelectionIdEnum.LAST_SIX_MONTH_AVG_DETAILED, "Last 6 Month Average Detailed", ChartType.COLUMNCHART, false, false);
        ChartSelection selection7 = new ChartSelection(ChartSelectionIdEnum.LAST_YEAR_AVG_DETAILED, "Last Year Average Detailed", ChartType.COLUMNCHART, false, false);
        chartSelectionRepository.save(selection1);
        chartSelectionRepository.save(selection2);
        chartSelectionRepository.save(selection3);
        chartSelectionRepository.save(selection4);
        chartSelectionRepository.save(selection5);
        chartSelectionRepository.save(selection6);
        chartSelectionRepository.save(selection7);
        ChartSelection selection9 = new ChartSelection(ChartSelectionIdEnum.USER_CURRENT_MONTH, "Current Month", ChartType.PIECHART, false, true);
        ChartSelection selection10 = new ChartSelection(ChartSelectionIdEnum.USER_PREV_MONTH, "Previous Month", ChartType.PIECHART, false, true);
        ChartSelection selection11 = new ChartSelection(ChartSelectionIdEnum.USER_PREV_THREE_MONTH_AVG, "Previous 3 Month Average", ChartType.PIECHART, false, true);
        ChartSelection selection12 = new ChartSelection(ChartSelectionIdEnum.USER_PREV_THREE_MONTH_TOTAL, "Previous 3 Month Total", ChartType.PIECHART, false, true);
        ChartSelection selection13 = new ChartSelection(ChartSelectionIdEnum.USER_CUSTOM_PERIOD_AVG, "Custom Period Average", ChartType.PIECHART, true, true);
        ChartSelection selection14 = new ChartSelection(ChartSelectionIdEnum.USER_CUSTOM_PERIOD_TOTAL, "Custom Period Total", ChartType.PIECHART, true, true);

        ChartSelection selection15 = new ChartSelection(ChartSelectionIdEnum.USER_LAST_THREE_MONTH_DETAILED, "Last 3 Month Average Detailed", ChartType.COLUMNCHART, false, true);
        ChartSelection selection16 = new ChartSelection(ChartSelectionIdEnum.USER_LAST_SIX_MONTH_DETAILED, "Last 6 Month Average Detailed", ChartType.COLUMNCHART, false, true);
        ChartSelection selection17 = new ChartSelection(ChartSelectionIdEnum.USER_LAST_YEAR_DETAILED, "Last Year Average Detailed", ChartType.COLUMNCHART, false, true);
        ChartSelection selection18 = new ChartSelection(ChartSelectionIdEnum.USER_CUSTOM_PERIOD_DETAILED, "Custom Period Average Detailed", ChartType.COLUMNCHART, true, true);
        chartSelectionRepository.save(selection9);
        chartSelectionRepository.save(selection10);
        chartSelectionRepository.save(selection11);
        chartSelectionRepository.save(selection12);
        chartSelectionRepository.save(selection13);
        chartSelectionRepository.save(selection14);
        chartSelectionRepository.save(selection15);
        chartSelectionRepository.save(selection16);
        chartSelectionRepository.save(selection17);
        chartSelectionRepository.save(selection18);
        System.out.println(String.format("ChartSelections loaded in %dms", System.currentTimeMillis() - start));
    }

    private String[] fileReader(String pathToFile) {
        try {
            FileReader fileReader;
            BufferedReader br;
            ArrayList<String> lines = new ArrayList<>();
            fileReader = new FileReader(new File(pathToFile));
            br = new BufferedReader(fileReader);
            String line = null;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            br.close();
            String[] res = new String[lines.size()];
            lines.toArray(res);
            return res;
        } catch (IOException e) {
            return null;
        }
    }

    private GeneralCategory getRandomCategory(List<GeneralCategory> db) {
        return db.get(getRandomInteger(0, db.size()));
    }

    private int getRandomInteger(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    private double getRandomAmount(double min, double max) {
        return Math.round(ThreadLocalRandom.current().nextDouble(min, max) * 100.0) / 100.0;
    }
}
