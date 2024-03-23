package mejai.mejaigg.repository;

import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import jakarta.transaction.Transactional;
import mejai.mejaigg.domain.User;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@Test
	public void findOneWithNameAndTag() {
		//given
		String name = "Hide on bush";
		String tag = "KR1";
		//when
		Optional<User> bySummonerNameAndTagLineAllIgnoreCase = userRepository.findBySummonerNameAndTagLineAllIgnoreCase(
			name, tag);
		if (bySummonerNameAndTagLineAllIgnoreCase.isPresent()) {
			User user = bySummonerNameAndTagLineAllIgnoreCase.get();
			System.out.println("user = " + user);
		}
		//then
		assertTrue(bySummonerNameAndTagLineAllIgnoreCase.isPresent());
	}
}
