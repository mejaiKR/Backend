package mejai.mejaigg.match.repository;

import mejai.mejaigg.match.domain.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long> {
}
