package com.kamabizbazti.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kamabizbazti.model.dao.ChartSelection;
import com.kamabizbazti.model.dao.Currency;
import com.kamabizbazti.model.dao.GeneralCategory;
import com.kamabizbazti.model.dao.Language;
import com.kamabizbazti.model.repository.ChartSelectionRepository;
import com.kamabizbazti.model.repository.CurrencyRepository;
import com.kamabizbazti.model.repository.GeneralCategoryRepository;
import com.kamabizbazti.model.repository.LanguageRepository;
import com.kamabizbazti.security.entities.Authority;
import com.kamabizbazti.security.repository.AuthorityRepository;
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
        for (ChartSelection selection : selections)
            chartSelectionRepository.save(selection);
    }

    private void insertLanguages() throws IOException {
        Iterable<Language> languages = mapper.readValue(new File(classLoader.getResource(DEFAULT_LANGUAGES).getFile()), new TypeReference<Iterable<Language>>() {
        });
        for (Language language : languages)
            languageRepository.save(language);
    }

    private void insertCurrencies() throws IOException {
        Iterable<Currency> currencies = mapper.readValue(new File(classLoader.getResource(DEFAULT_CURRENCIES).getFile()), new TypeReference<Iterable<Currency>>() {
        });
        for (Currency currency : currencies)
            currencyRepository.save(currency);
    }

    private void insertAuthorities() throws IOException {
        Iterable<Authority> authorities = mapper.readValue(new File(classLoader.getResource(DEFAULT_AUTHORITIES).getFile()), new TypeReference<Iterable<Authority>>() {
        });
        for (Authority authority : authorities)
            authorityRepository.save(authority);
    }

    private void insertCategories() throws IOException {
        Iterable<GeneralCategory> categories = mapper.readValue(new File(classLoader.getResource(DEFAULT_CATEGORIES).getFile()), new TypeReference<Iterable<GeneralCategory>>() {
        });
        for (GeneralCategory category : categories)
            generalCategoryRepository.save(category);
    }
}
