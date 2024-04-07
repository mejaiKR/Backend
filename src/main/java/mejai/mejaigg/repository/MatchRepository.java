package mejai.mejaigg.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mejai.mejaigg.domain.Match;

public interface MatchRepository extends JpaRepository<Match, String> {
}
