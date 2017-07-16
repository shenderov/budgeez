package com.budgeez;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BudgeezBootApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(BudgeezBootApplication.class, args);
    }
}
