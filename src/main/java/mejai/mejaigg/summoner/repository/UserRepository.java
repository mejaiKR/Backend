package mejai.mejaigg.summoner.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import mejai.mejaigg.summoner.entity.User;

public interface UserRepository extends JpaRepository<User, String>, UserRepositoryCustom {

	Optional<User> findById(String puuid);

	Optional<User> findBySummonerNameAndTagLineAllIgnoreCase(String name, String tag);
}
