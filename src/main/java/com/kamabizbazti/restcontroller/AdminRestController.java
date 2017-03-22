package com.kamabizbazti.restcontroller;


import com.kamabizbazti.model.dao.Currency;
import com.kamabizbazti.model.dao.Language;
import com.kamabizbazti.model.repository.CurrencyRepository;
import com.kamabizbazti.model.repository.LanguageRepository;
import com.kamabizbazti.security.entities.Authority;
import com.kamabizbazti.security.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "admin")
@PreAuthorize("hasRole('ADMIN')")
@RestController
public class AdminRestController {

    @Autowired
    CurrencyRepository currencyRepository;

    @Autowired
    LanguageRepository languageRepository;

    @Autowired
    AuthorityRepository authorityRepository;


    @RequestMapping(value = "/addCurrency", method = RequestMethod.POST)
    public Currency addCurrency(@RequestBody Currency currency) {
        return currencyRepository.save(currency);
    }

    @RequestMapping(value = "/addLanguage", method = RequestMethod.POST)
    public Language addLanguage(@RequestBody Language language) {
        return languageRepository.save(language);
    }

    @RequestMapping(value = "/addAuthority", method = RequestMethod.POST)
    public Authority addLanguage(@RequestBody Authority authority) {
        return authorityRepository.save(authority);
    }


    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public String test(@RequestBody String token) {
        System.out.println(token);
        return "Test token admin: " + token;
    }
}
