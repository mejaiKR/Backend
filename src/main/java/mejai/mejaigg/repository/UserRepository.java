package mejai.mejaigg.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import mejai.mejaigg.domain.User;

public interface UserRepository extends JpaRepository<User, String> {

	Optional<User> findById(String puuid);

	// Custom query using JPQL for finding a user with ranks
	@Query("select u from User u join fetch u.rank r where u.puuid = :puuid")
	Optional<List<User>> findOneWithRankByPuuid(String puuid);

	Optional<User> findBySummonerNameAndTagLineAllIgnoreCase(String name, String tag);
}
