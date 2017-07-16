package com.budgeez;

import com.budgeez.common.DataGenerator;
import com.budgeez.common.TestConfiguration;
import com.budgeez.common.TestTools;
import com.budgeez.common.database.DatabaseConnector;
import com.budgeez.common.http.AuthenticationRestControllerConnectorHelper;
import com.budgeez.common.http.GeneralRestControllerConnectorHelper;
import com.budgeez.common.http.UserRestControllerConnectorHelper;
import com.budgeez.common.mail.MailClient;
import com.budgeez.config.BudgeezApplicationConfig;
import com.budgeez.model.interfaces.IDateHelper;
import com.budgeez.model.interfaces.IMailingService;
import com.budgeez.model.interfaces.ITextHelper;
import com.budgeez.model.repository.*;
import com.budgeez.security.entities.Authority;
import com.budgeez.security.entities.AuthorityName;
import com.budgeez.security.repository.AuthorityRepository;
import com.budgeez.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;

import java.sql.Connection;
import java.util.Set;
import java.util.TreeSet;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@ContextConfiguration(classes = {BudgeezBootApplication.class, BudgeezApplicationConfig.class, TestConfiguration.class})
@TestPropertySource(locations="classpath:test.properties")
public abstract class BudgeezBootApplicationTests extends AbstractTestNGSpringContextTests {

    @LocalServerPort
    protected int port = 0;

    @Value(value = "${test.env.hostname}")
    protected String hostname;

    @Value(value = "${test.env.base_uri}")
    protected String baseUri;

    @Value(value = "${spring.datasource.driver-class-name}")
    protected String dbDriverName;

    @Value(value = "${spring.datasource.url}")
    protected String databaseUrl;

    @Value(value = "${spring.datasource.username}")
    protected String dbUsername;

    @Value(value = "${spring.datasource.password}")
    protected String dbPassword;

    @Autowired
    protected LanguageRepository languageRepository;

    @Autowired
    protected CurrencyRepository currencyRepository;

    @Autowired
    protected GeneralCategoryRepository generalCategoryRepository;

    @Autowired
    protected CustomCategoryRepository customCategoryRepository;

    @Autowired
    protected AuthorityRepository authorityRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected RecordRepository recordRepository;

    @Autowired
    protected ChartSelectionRepository chartSelectionRepository;

    @Autowired
    protected ActivationTokenRepository activationTokenRepository;

    @Autowired
    protected VerificationTokenRepository verificationTokenRepository;

    @Autowired
    protected IMailingService mailingService;

    @Autowired
    protected MailClient mailClient;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected DataGenerator dataGenerator;

    @Autowired
    protected TestTools testTools;

    @Autowired
    protected IDateHelper dateHelper;

    @Autowired
    protected ITextHelper textHelper;

    protected AuthenticationRestControllerConnectorHelper authenticationRestControllerConnectorHelper;
    protected GeneralRestControllerConnectorHelper generalRestControllerConnectorHelper;
    protected UserRestControllerConnectorHelper userRestControllerConnectorHelper;
    protected Connection databaseConnection;

    @BeforeClass
    public void init() throws Exception {
        authenticationRestControllerConnectorHelper = new AuthenticationRestControllerConnectorHelper(hostname, port, baseUri);
        generalRestControllerConnectorHelper = new GeneralRestControllerConnectorHelper(hostname, port, baseUri);
        userRestControllerConnectorHelper = new UserRestControllerConnectorHelper(hostname, port, baseUri);
        databaseConnection = DatabaseConnector.getDatabaseConnection(dbDriverName, databaseUrl, dbUsername, dbPassword);
    }

    protected Set<Authority> setUserAuthorities() {
        Set<Authority> userAuthorities = new TreeSet<>();
        userAuthorities.add(authorityRepository.findByName(AuthorityName.ROLE_USER));
        return userAuthorities;
    }

}
