package mejai.mejaigg.match.usergamestat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mejai.mejaigg.match.usergamestat.entity.UserGameStat;

public interface UserGameStatRepository extends JpaRepository<UserGameStat, Long> {
}
