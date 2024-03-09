package mejai.mejaigg.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import mejai.mejaigg.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class userServiceTest {
	@Autowired
	EntityManager em;
	@Autowired
	UserService userService;
	@Autowired
	UserRepository userRepository;

	@Test
	public void 라이엇_API응답_테스트(){
		SummonerDTO 꺄아르륵 = userService.summonerTest("꺄아르륵");
		System.out.println("꺄아르륵 = " + 꺄아르륵);

	}

}
