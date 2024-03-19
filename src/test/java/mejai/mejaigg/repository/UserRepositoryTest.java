package mejai.mejaigg.repository;

import jakarta.transaction.Transactional;
import mejai.mejaigg.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserRepositoryTest {

	@Autowired
	private  UserRepository userRepository;
	@Test
	public void findOneWithNameAndTag() {
		//given
		String name = "Hide on bush";
		String tag = "KR1";
		//when
		User user = userRepository.findOneWithNameAndTag(name, tag);
		//then
		assertNotNull(user);
	}
}
