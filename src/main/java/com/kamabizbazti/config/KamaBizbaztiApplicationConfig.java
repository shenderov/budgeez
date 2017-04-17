package com.kamabizbazti.config;

import com.kamabizbazti.model.handlers.GeneralRequestHandler;
import com.kamabizbazti.model.handlers.GeneralStatisticsHandler;
import com.kamabizbazti.model.handlers.UserRequestHandler;
import com.kamabizbazti.model.handlers.UserStatisticsHandler;
import com.kamabizbazti.model.helpers.DateHelper;
import com.kamabizbazti.model.helpers.ExceptionMessagesHelper;
import com.kamabizbazti.model.helpers.SystemHelper;
import com.kamabizbazti.model.helpers.ValidationHelper;
import com.kamabizbazti.model.interfaces.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.CharacterEncodingFilter;

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
    public IValidationHelper validationHelper() {
        return new ValidationHelper();
    }

    @Bean
    public IExceptionMessagesHelper exceptionMessagesHelper() {
        return new ExceptionMessagesHelper();
    }

    @Bean
    public ISystemHelper systemHelper() {
        return new SystemHelper();
    }

    @Bean(name = "messageSource")
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageBundle = new ReloadableResourceBundleMessageSource();
        messageBundle.setBasename("classpath:messages/messages");
        messageBundle.setDefaultEncoding("UTF-8");
        return messageBundle;
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    CharacterEncodingFilter characterEncodingFilter(){
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return filter;
    }
}
