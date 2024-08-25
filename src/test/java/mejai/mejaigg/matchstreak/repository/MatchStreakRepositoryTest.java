package mejai.mejaigg.matchstreak.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.time.YearMonth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import mejai.mejaigg.global.util.TestUtil;
import mejai.mejaigg.matchstreak.domain.MatchStreak;
import mejai.mejaigg.searchhistory.domain.SearchHistory;
import mejai.mejaigg.searchhistory.repository.SearchHistoryRepository;
import mejai.mejaigg.summoner.domain.Summoner;
import mejai.mejaigg.summoner.repository.SummonerRepository;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class MatchStreakRepositoryTest {
	@Autowired
	private MatchStreakRepository matchStreakRepository;

	@Autowired
	SearchHistoryRepository searchHistoryRepository;

	@Autowired
	SummonerRepository summonerRepository;

	private Long searchHistoryId;
	private Long userId;

	@BeforeEach
	void setUp() {
		Summoner summoner = TestUtil.createTestUser();
		summonerRepository.saveAndFlush(summoner);

		SearchHistory searchHistory = TestUtil.createTestSearchHistory(summoner, YearMonth.of(2021, 1));
		searchHistoryRepository.save(searchHistory);

		userId = summoner.getId();
		searchHistoryId = searchHistory.getId();
	}

	@Test
	@DisplayName("MatchDateStreak를 생성한다.")
	void createMatchDateStreak() {
		// given
		MatchStreak matchStreak = MatchStreak.builder()
			.date(LocalDate.of(2021, 1, 1))
			.build();
		// when
		matchStreakRepository.save(matchStreak);
		// then
		assertThat(matchStreak.getId()).isNotNull();
		assertThat(matchStreak.getDate()).isEqualTo(LocalDate.of(2021, 1, 1));
	}

	@Test
	@DisplayName("HistoryId로 해당 날짜에 해당하는 MatchDateStreak를 찾는다.")
	void findByDateAndSearchHistory() {
		// given
		SearchHistory searchHistory = searchHistoryRepository.findById(searchHistoryId).orElseThrow();
		MatchStreak matchStreak = MatchStreak.builder()
			.date(LocalDate.of(2021, 1, 1))
			.build();
		// when
		searchHistory.addMatchDateStreak(matchStreak);
		matchStreakRepository.save(matchStreak);
		// then
		MatchStreak found = matchStreakRepository.findByDateAndSearchHistory(LocalDate.of(2021, 1, 1),
				searchHistoryId)
			.orElseThrow();
		assertThat(found.getDate()).isEqualTo(LocalDate.of(2021, 1, 1));
		assertThat(found.getSearchHistory().getId()).isEqualTo(searchHistoryId);
		assertThat(found.getSearchHistory().getSummoner().getId()).isEqualTo(userId);
	}

	@Test
	@DisplayName("HistoryId로 해당 날짜에 해당하는 MatchDateStreak를 찾지 못한다.")
	void findByDateAndSearchHistoryFail() {
		// given
		SearchHistory searchHistory = searchHistoryRepository.findById(searchHistoryId).orElseThrow();
		MatchStreak matchStreak = MatchStreak.builder()
			.date(LocalDate.of(2021, 1, 1))
			.build();
		// when
		searchHistory.addMatchDateStreak(matchStreak);
		matchStreakRepository.save(matchStreak);
		// then
		assertThat(matchStreakRepository.findByDateAndSearchHistory(LocalDate.of(2021, 1, 2), searchHistoryId))
			.isEmpty();
	}

}
