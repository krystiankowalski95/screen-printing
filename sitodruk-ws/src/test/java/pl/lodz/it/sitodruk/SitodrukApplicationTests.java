package pl.lodz.it.sitodruk;

import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import pl.lodz.it.sitodruk.config.JwtUtils;


@RunWith(MockitoJUnitRunner.class)
class SitodrukApplicationTests {

	JwtUtils jwtUtils;

	@Before
	void init(){
	    jwtUtils = new JwtUtils();
	}

	@Test
	void testMethod() {
		Assert.assertEquals(false, false);
	}

}
