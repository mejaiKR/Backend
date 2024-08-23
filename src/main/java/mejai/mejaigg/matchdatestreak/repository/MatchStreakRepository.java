package mejai.mejaigg.matchdatestreak.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import mejai.mejaigg.matchdatestreak.domain.MatchStreak;

public interface MatchStreakRepository extends JpaRepository<MatchStreak, Long> {
	@Query("select mds from MatchStreak mds where mds.date = :date "
		+ "and mds.searchHistory.id = :searchHistoryId")
	Optional<MatchStreak> findByDateAndSearchHistory(LocalDate date, Long searchHistoryId);
}
