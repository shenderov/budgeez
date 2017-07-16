package com.budgeez.common;

import com.budgeez.common.mail.MailClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfiguration {

    static final String TOKEN_HEADER = "Authorization";

    //General URLs
    private static final String GENERAL_PATH = "/general";
    public static final String GET_GENERAL_CHART_SELECTIONS_LIST = GENERAL_PATH + "/getGeneralChartSelectionsList";
    public static final String GET_USER_CHART_SELECTIONS_LIST = GENERAL_PATH + "/getUserChartSelectionsList";
    public static final String GET_DEFAULT_DATATABLE = GENERAL_PATH + "/getDefaultDataTable";
    public static final String GET_GENERAL_DATATABLE = GENERAL_PATH + "/getGeneralDataTable";
    public static final String GET_VERSION = GENERAL_PATH + "/getVersion";
    public static final String GET_LANGUAGES = GENERAL_PATH + "/getLanguages";
    public static final String GET_CURRENCIES = GENERAL_PATH + "/getCurrencies";

    //Authentication URLs
    public static final String LOGIN =  "/login";
    public static final String SIGNUP =  "/signup";
    public static final String REFRESH = "/refresh";
    public static final String GET_USER_DETAILS = "/getUserDetails";
    public static final String CHANGE_EMAIL = "/changeEmail";
    public static final String IS_EMAIL_REGISTERED = "/isEmailRegistered";

    //User URLs
    private static final String USER_PATH = "/user";
    public static final String GET_CATEGORIES_LIST = USER_PATH + "/getCategoriesList";
    public static final String GET_USER_DEFAULT_DATATABLE = USER_PATH + "/getUserDefaultDataTable";
    public static final String GET_USER_DATATABLE = USER_PATH + "/getUserDataTable";
    public static final String ADD_RECORD = USER_PATH + "/addRecord";
    public static final String ADD_CUSTOM_CATEGORY = USER_PATH + "/addCustomCategory";
    public static final String GET_RECORDS_LIST = USER_PATH + "/getRecordsList";
    public static final String DELETE_RECORD = USER_PATH + "/deleteRecord";
    public static final String EDIT_RECORD = USER_PATH + "/editRecord";

    //Mail client
    public static final String MAIL_USERNAME = "budgeez@gmail.com";
    public static final String MAIL_PASSWORD = "KaMaBiZbAzTi2017";
    public static final String MAIL_POP3_HOST = "pop.gmail.com";
    public static final String MAIL_POP3_PORT = "995";
    public static final String MAIL_POP3_STARTTLS_ENABLE = "true";

    @Bean
    public TestTools testTools() {
        return new TestTools();
    }

    @Bean
    public DataGenerator dataGenerator() {
        return new DataGenerator();
    }

    @Bean
    public MailClient mailClient(){
        return new MailClient();
    }
}
