package com.kamabizbazti;

import com.kamabizbazti.common.DataGenerator;
import com.kamabizbazti.common.TestConfiguration;
import com.kamabizbazti.common.TestTools;
import com.kamabizbazti.common.database.DatabaseConnector;
import com.kamabizbazti.common.http.AuthenticationRestControllerConnectorHelper;
import com.kamabizbazti.common.http.GeneralRestControllerConnectorHelper;
import com.kamabizbazti.common.http.UserRestControllerConnectorHelper;
import com.kamabizbazti.config.KamaBizbaztiApplicationConfig;
import com.kamabizbazti.model.interfaces.IDateHelper;
import com.kamabizbazti.model.repository.*;
import com.kamabizbazti.security.repository.AuthorityRepository;
import com.kamabizbazti.security.repository.UserRepository;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@ContextConfiguration(classes = {KamaBizbaztiBootApplication.class, KamaBizbaztiApplicationConfig.class, TestConfiguration.class})
@TestPropertySource(locations="classpath:test.properties")
public abstract class KamaBizbaztiBootApplicationTests extends AbstractTestNGSpringContextTests {

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
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected DataGenerator dataGenerator;

    @Autowired
    protected TestTools testTools;

    @Autowired
    protected IDateHelper dateHelper;

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

}
