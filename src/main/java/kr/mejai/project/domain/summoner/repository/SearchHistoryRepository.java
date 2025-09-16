package kr.mejai.project.domain.summoner.repository;

import java.time.YearMonth;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.mejai.project.domain.summoner.entity.SearchHistory;
import kr.mejai.project.domain.summoner.entity.Summoner;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
	Optional<SearchHistory> findBySummonerAndDate(Summoner summoner, YearMonth date);
}
