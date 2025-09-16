package kr.mejai.project.matchstreak.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kr.mejai.project.domain.matchstreak.domain.MatchStreak;
import kr.mejai.project.domain.matchstreak.repository.MatchStreakRepository;
import kr.mejai.project.domain.matchstreak.service.StreakService;
import kr.mejai.project.domain.rank.repository.RankRepository;
import kr.mejai.project.domain.searchhistory.domain.SearchHistory;
import kr.mejai.project.domain.searchhistory.repository.SearchHistoryRepository;
import kr.mejai.project.domain.summoner.domain.Summoner;
import kr.mejai.project.domain.summoner.dto.request.SummonerStreakRequest;
import kr.mejai.project.domain.summoner.dto.response.SummonerStreakResponse;
import kr.mejai.project.domain.summoner.repository.SummonerRepository;

@SpringBootTest
@DisplayName("스트릭 테스트")
@Transactional
class StreakServiceTest {
	private static final Logger log = LoggerFactory.getLogger(StreakServiceTest.class);
	@Autowired
	private StreakService streakService;

	@Autowired
	private MatchStreakRepository matchStreakRepository;

	@Autowired
	private SearchHistoryRepository searchHistoryRepository;

	@Autowired
	private SummonerRepository summonerRepository;

	@Autowired
	private RankRepository rankRepository;

	@Test
	@DisplayName("스트릭이 있는데 조회를 하면 조회된다.")
	void testGetStreakSuccess() {
		// given
		Summoner summoner = Summoner.builder()
			.summonerName("testName")
			.tagLine("testTag")
			.puuid("testPuuid")
			.summonerLevel(1L)
			.profileIconId(1)
			.revisionDate(1L)
			.build();
		summonerRepository.save(summoner);

		SearchHistory searchHistory = SearchHistory.builder()
			.summoner(summoner)
			.date(YearMonth.of(2021, 8))
			.done(true)
			.build();
		searchHistoryRepository.save(searchHistory);
		List<MatchStreak> matchStreaks = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			MatchStreak matchStreak = MatchStreak.builder()
				.searchHistory(searchHistory)
				.date(LocalDate.of(2021, 8, i + 1))
				.allGameCount(10)
				.build();
			searchHistory.getMatchStreaks().add(matchStreak);
			matchStreaks.add(matchStreak);
		}
		matchStreakRepository.saveAll(matchStreaks);
		searchHistoryRepository.save(searchHistory);

		SummonerStreakRequest request = SummonerStreakRequest.builder()
			.id("testName")
			.tag("testTag")
			.year(2021)
			.month(8)
			.build();

		// when
		SummonerStreakResponse streak = streakService.getStreak(
			request.getId(),
			request.getTag(),
			request.getYear(),
			request.getMonth()
		);
		// then
		assertNotNull(streak);
		assertEquals(20, streak.getUserGameCounts().size());
	}
}
