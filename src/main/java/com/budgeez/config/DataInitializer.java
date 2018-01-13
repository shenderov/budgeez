package com.budgeez.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.budgeez.model.entities.dao.ChartSelection;
import com.budgeez.model.entities.dao.Currency;
import com.budgeez.model.entities.dao.GeneralCategory;
import com.budgeez.model.entities.dao.Language;
import com.budgeez.model.repository.ChartSelectionRepository;
import com.budgeez.model.repository.CurrencyRepository;
import com.budgeez.model.repository.GeneralCategoryRepository;
import com.budgeez.model.repository.LanguageRepository;
import com.budgeez.security.entities.Authority;
import com.budgeez.security.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

public class DataInitializer {

    @Autowired
    private ChartSelectionRepository chartSelectionRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private GeneralCategoryRepository generalCategoryRepository;

    private ObjectMapper mapper = new ObjectMapper();
    private ClassLoader classLoader = getClass().getClassLoader();

    private static final String DEFAULT_CHART_SELECTIONS = "default_selections.json";
    private static final String DEFAULT_LANGUAGES = "default_languages.json";
    private static final String DEFAULT_CURRENCIES = "default_currencies.json";
    private static final String DEFAULT_AUTHORITIES = "default_authorities.json";
    private static final String DEFAULT_CATEGORIES = "default_categories.json";

    @PostConstruct
    public void initiateData() throws IOException {
        if (chartSelectionRepository.count() == 0)
            insertChartSelections();
        if (languageRepository.count() == 0)
            insertLanguages();
        if (currencyRepository.count() == 0)
            insertCurrencies();
        if (authorityRepository.count() == 0)
            insertAuthorities();
        if (generalCategoryRepository.count() == 0)
            insertCategories();
    }

    private void insertChartSelections() throws IOException {
        Iterable<ChartSelection> selections = mapper.readValue(new File(classLoader.getResource(DEFAULT_CHART_SELECTIONS).getFile()), new TypeReference<Iterable<ChartSelection>>() {
        });
        chartSelectionRepository.save(selections);
    }

    private void insertLanguages() throws IOException {
        Iterable<Language> languages = mapper.readValue(new File(classLoader.getResource(DEFAULT_LANGUAGES).getFile()), new TypeReference<Iterable<Language>>() {
        });
        languageRepository.save(languages);
    }

    private void insertCurrencies() throws IOException {
        Iterable<Currency> currencies = mapper.readValue(new File(classLoader.getResource(DEFAULT_CURRENCIES).getFile()), new TypeReference<Iterable<Currency>>() {
        });
        currencyRepository.save(currencies);
    }

    private void insertAuthorities() throws IOException {
        Iterable<Authority> authorities = mapper.readValue(new File(classLoader.getResource(DEFAULT_AUTHORITIES).getFile()), new TypeReference<Iterable<Authority>>() {
        });
        authorityRepository.save(authorities);
    }

    private void insertCategories() throws IOException {
        Iterable<GeneralCategory> categories = mapper.readValue(new File(classLoader.getResource(DEFAULT_CATEGORIES).getFile()), new TypeReference<Iterable<GeneralCategory>>() {
        });
        generalCategoryRepository.save(categories);
    }
}
