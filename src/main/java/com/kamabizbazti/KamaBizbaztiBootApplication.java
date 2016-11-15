package com.kamabizbazti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class KamaBizbaztiBootApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(KamaBizbaztiBootApplication.class, args);
	}
}
