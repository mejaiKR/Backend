package mejai.mejaigg.matchstreak.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import mejai.mejaigg.matchstreak.domain.MatchStreak;
import mejai.mejaigg.searchhistory.domain.SearchHistory;

public interface MatchStreakRepository extends JpaRepository<MatchStreak, Long> {
	@Query("select mds from MatchStreak mds where mds.date = :date "
		+ "and mds.searchHistory.id = :searchHistoryId")
	Optional<MatchStreak> findByDateAndSearchHistory(LocalDate date, Long searchHistoryId);

	Optional<MatchStreak> findBySearchHistoryAndDate(SearchHistory sh, LocalDate date);
}
