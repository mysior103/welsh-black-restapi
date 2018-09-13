package pl.mysior.welshblackrestapi;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WelshBlackRestapiApplicationTests {
	@Autowired
	private WelshBlackRestapiApplication welshBlackRestapiApplication;
	@Test
	public void contextLoads() {
		Assert.assertNotNull(welshBlackRestapiApplication);
	}

	@Test
	public void creatingBCryptPasswordEncoderBean(){
		Assert.assertNotNull(welshBlackRestapiApplication.bCryptPasswordEncoder());
	}
}
