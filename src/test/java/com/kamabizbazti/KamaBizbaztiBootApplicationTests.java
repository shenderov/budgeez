package com.kamabizbazti;

import com.kamabizbazti.config.KamaBizbaztiApplicationConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

@SpringBootTest
@ContextConfiguration(classes = {KamaBizbaztiBootApplication.class, KamaBizbaztiApplicationConfig.class})
@TestPropertySource(locations="classpath:test.properties")
public class KamaBizbaztiBootApplicationTests extends AbstractTestNGSpringContextTests {

}
