package com.budgeez.common;

import com.budgeez.model.entities.dao.*;
import com.budgeez.model.entities.dao.Currency;
import com.budgeez.model.repository.*;
import com.budgeez.security.entities.Authority;
import com.budgeez.model.entities.dao.User;
import com.budgeez.security.repository.AuthorityRepository;
import com.budgeez.security.repository.UserRepository;
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

    public void insertUsers(UserRepository userRepository, LanguageRepository languageRepository, CurrencyRepository currencyRepository, AuthorityRepository authorityRepository, PasswordEncoder passwordEncoder, int count) {
        long start = System.currentTimeMillis();
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
