package mejai.mejaigg.repository;

import java.sql.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import mejai.mejaigg.domain.MatchDateStreak;

public interface MatchDateStreakRepository extends JpaRepository<MatchDateStreak, Long> {
	@Query("select mds from MatchDateStreak mds where mds.date = :date and mds.searchHistory.historyId = :searchHistoryId")
	Optional<MatchDateStreak> findByDateAndSearchHistory(Date date, Long searchHistoryId);
}
