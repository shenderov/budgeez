package com.kamabizbazti.config;

import com.kamabizbazti.DataGenerator;
import com.kamabizbazti.model.handlers.GeneralRequestHandler;
import com.kamabizbazti.model.handlers.GeneralStatisticsHandler;
import com.kamabizbazti.model.handlers.UserRequestHandler;
import com.kamabizbazti.model.handlers.UserStatisticsHandler;
import com.kamabizbazti.model.helpers.DateHelper;
import com.kamabizbazti.model.interfaces.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KamaBizbaztiApplicationConfig {

    @Bean
    public DataInitializer dataInitializer() {
        return new DataInitializer();
    }

    @Bean
    public IGeneralRequestHandler handler() {
        return new GeneralRequestHandler();
    }

    @Bean
    public IGeneralStatisticsHandler generalStatisticsHandler() {
        return new GeneralStatisticsHandler();
    }

    @Bean
    public IUserRequestHandler userRequestHandler() {
        return new UserRequestHandler();
    }

    @Bean
    public IUserStatisticsHandler userStatisticsHandler() {
        return new UserStatisticsHandler();
    }

    @Bean
    public IDateHelper dateHelper() {
        return new DateHelper();
    }

    @Bean
    public DataGenerator dataGenerator() {
        return new DataGenerator();
    }
}
