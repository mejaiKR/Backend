package mejai.mejaigg.service;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import mejai.mejaigg.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserServiceTest {
	@Autowired
	EntityManager em;
	@Autowired
	UserService userService;
	@Autowired
	UserRepository userRepository;

	// @Test
	// @Rollback(false)
	// public void apiResponseTest() {
	// 	userService.setUserProfile("hide on bush", "kr1");
	// }

}
