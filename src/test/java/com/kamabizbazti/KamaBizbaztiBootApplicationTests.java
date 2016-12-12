package com.kamabizbazti;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testng.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KamaBizbaztiBootApplicationTests {

	@Test
	public void contextLoads() {
		Assert.assertEquals(true, true);
	}

}
