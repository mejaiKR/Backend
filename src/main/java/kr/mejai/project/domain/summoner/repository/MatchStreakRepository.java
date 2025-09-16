package kr.mejai.project.domain.summoner.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import kr.mejai.project.domain.summoner.entity.MatchStreak;
import kr.mejai.project.domain.summoner.entity.SearchHistory;

public interface MatchStreakRepository extends JpaRepository<MatchStreak, Long> {
	@Query("select mds from MatchStreak mds where mds.date = :date "
		+ "and mds.searchHistory.id = :searchHistoryId")
	Optional<MatchStreak> findByDateAndSearchHistory(LocalDate date, Long searchHistoryId);

	Optional<MatchStreak> findBySearchHistoryAndDate(SearchHistory sh, LocalDate date);

	List<MatchStreak> findAllBySearchHistoryAndDateBetween(
		SearchHistory history,
		LocalDate start,
		LocalDate end
	);
}
