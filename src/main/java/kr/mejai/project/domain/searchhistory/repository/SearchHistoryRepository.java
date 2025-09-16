package kr.mejai.project.domain.searchhistory.repository;

import java.time.YearMonth;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.mejai.project.domain.searchhistory.domain.SearchHistory;
import kr.mejai.project.domain.summoner.domain.Summoner;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
	Optional<SearchHistory> findBySummonerAndDate(Summoner summoner, YearMonth date);
}
