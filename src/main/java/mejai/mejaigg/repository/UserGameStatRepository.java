package mejai.mejaigg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mejai.mejaigg.domain.UserGameStat;

public interface UserGameStatRepository extends JpaRepository<UserGameStat, Long> {
}
