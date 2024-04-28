package mejai.mejaigg.match.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mejai.mejaigg.match.entity.Match;

public interface MatchRepository extends JpaRepository<Match, String> {
}
