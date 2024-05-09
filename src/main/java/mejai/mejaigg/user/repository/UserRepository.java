package mejai.mejaigg.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import mejai.mejaigg.user.entity.User;

public interface UserRepository extends JpaRepository<User, String>, UserRepositoryCustom {

	Optional<User> findById(String puuid);
	
	Optional<User> findBySummonerNameAndTagLineAllIgnoreCase(String name, String tag);
}
