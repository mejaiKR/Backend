package mejai.mejaigg.matchdatestreak.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import mejai.mejaigg.matchdatestreak.entity.MatchDateStreak;

public interface MatchDateStreakRepository extends JpaRepository<MatchDateStreak, Long> {
	@Query("select mds from MatchDateStreak mds where mds.date = :date "
		+ "and mds.searchHistory.id = :searchHistoryId")
	Optional<MatchDateStreak> findByDateAndSearchHistory(LocalDate date, Long searchHistoryId);
}
