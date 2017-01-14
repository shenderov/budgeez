package com.kamabizbazti;

import com.kamabizbazti.config.KamaBizbaztiApplicationConfig;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration(classes = {KamaBizbaztiBootApplication.class, KamaBizbaztiApplicationConfig.class})
@TestPropertySource(locations="classpath:test.properties")
public class KamaBizbaztiBootApplicationTests extends AbstractTestNGSpringContextTests {

	@Test
	public void contextLoads() {
	}




}
