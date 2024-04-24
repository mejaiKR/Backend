package mejai.mejaigg.match.userGameStat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mejai.mejaigg.match.userGameStat.entity.UserGameStat;

public interface UserGameStatRepository extends JpaRepository<UserGameStat, Long> {
}
