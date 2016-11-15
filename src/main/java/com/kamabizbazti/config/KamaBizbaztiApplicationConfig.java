package com.kamabizbazti.config;

import com.kamabizbazti.DataGenerator;
import com.kamabizbazti.model.handlers.GeneralRequestHandler;
import com.kamabizbazti.model.handlers.GeneralStatisticsHandler;
import com.kamabizbazti.model.handlers.UserRequestHandler;
import com.kamabizbazti.model.handlers.UserStatisticsHandler;
import com.kamabizbazti.model.interfaces.IGeneralRequestHandler;
import com.kamabizbazti.model.interfaces.IUserRequestHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KamaBizbaztiApplicationConfig {

    @Bean
    public IGeneralRequestHandler handler() {
        return new GeneralRequestHandler();
    }

    @Bean
    public IUserRequestHandler userRequestHandler() {
        return new UserRequestHandler();
    }

    @Bean
    public DataGenerator dataGenerator() {
        return new DataGenerator();
    }

    @Bean
    public GeneralStatisticsHandler generalStatisticsHandler() {
        return new GeneralStatisticsHandler();
    }

    @Bean
    public UserStatisticsHandler userStatisticsHandler() {
        return new UserStatisticsHandler();
    }


}
