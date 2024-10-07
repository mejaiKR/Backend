package mejai.mejaigg.searchhistory.repository;

import java.time.YearMonth;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import mejai.mejaigg.searchhistory.domain.SearchHistory;
import mejai.mejaigg.summoner.domain.Summoner;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
	Optional<SearchHistory> findBySummonerAndDate(Summoner summoner, YearMonth date);
}
