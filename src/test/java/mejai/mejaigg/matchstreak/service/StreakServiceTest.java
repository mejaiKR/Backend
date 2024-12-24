package mejai.mejaigg.matchstreak.service;

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

import mejai.mejaigg.matchstreak.domain.MatchStreak;
import mejai.mejaigg.matchstreak.repository.MatchStreakRepository;
import mejai.mejaigg.rank.repository.RankRepository;
import mejai.mejaigg.searchhistory.domain.SearchHistory;
import mejai.mejaigg.searchhistory.repository.SearchHistoryRepository;
import mejai.mejaigg.summoner.domain.Summoner;
import mejai.mejaigg.summoner.dto.request.SummonerStreakRequest;
import mejai.mejaigg.summoner.dto.response.SummonerStreakResponse;
import mejai.mejaigg.summoner.repository.SummonerRepository;

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
			.summonerId("testSummonerId")
			.accountId("testAccountId")
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
