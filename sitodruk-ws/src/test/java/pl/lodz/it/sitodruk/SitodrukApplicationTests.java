package pl.lodz.it.sitodruk;

import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.lodz.it.sitodruk.config.JwtUtils;
import pl.lodz.it.sitodruk.model.UserEntity;
import pl.lodz.it.sitodruk.repositories.UserRepository;
import pl.lodz.it.sitodruk.service.UserService;

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
