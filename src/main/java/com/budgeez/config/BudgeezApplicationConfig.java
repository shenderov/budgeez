package com.budgeez.config;

import com.budgeez.model.handlers.GeneralRequestHandler;
import com.budgeez.model.handlers.GeneralStatisticsHandler;
import com.budgeez.model.handlers.UserRequestHandler;
import com.budgeez.model.handlers.UserStatisticsHandler;
import com.budgeez.model.helpers.*;
import com.budgeez.model.interfaces.*;
import com.budgeez.model.services.MailingService;
import com.budgeez.security.interfaces.IVerificationService;
import com.budgeez.security.service.VerificationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
public class BudgeezApplicationConfig {

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
    public ITextHelper textelper() {
        return new TextHelper();
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

    @Bean
    public IMailingService mailingService(){
        return new MailingService();
    }

    @Bean
    public IMailingHelper mailingHelper(){
        return new MailingHelper();
    }

    @Bean
    public IVerificationService verificationService(){
        return new VerificationService();
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
