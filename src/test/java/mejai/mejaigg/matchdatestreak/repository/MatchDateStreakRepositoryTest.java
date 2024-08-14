package mejai.mejaigg.matchdatestreak.repository;

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
import mejai.mejaigg.matchdatestreak.entity.MatchDateStreak;
import mejai.mejaigg.searchhistory.entity.SearchHistory;
import mejai.mejaigg.searchhistory.repository.SearchHistoryRepository;
import mejai.mejaigg.summoner.entity.User;
import mejai.mejaigg.summoner.repository.UserRepository;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class MatchDateStreakRepositoryTest {
	@Autowired
	private MatchDateStreakRepository matchDateStreakRepository;

	@Autowired
	SearchHistoryRepository searchHistoryRepository;

	@Autowired
	UserRepository userRepository;

	private Long searchHistoryId;
	private String userId;

	@BeforeEach
	void setUp() {
		User user = TestUtil.createTestUser();
		userRepository.saveAndFlush(user);

		SearchHistory searchHistory = TestUtil.createTestSearchHistory(user, YearMonth.of(2021, 1));
		searchHistoryRepository.save(searchHistory);

		userId = user.getId();
		searchHistoryId = searchHistory.getId();
	}

	@Test
	@DisplayName("MatchDateStreak를 생성한다.")
	void createMatchDateStreak() {
		// given
		MatchDateStreak matchDateStreak = MatchDateStreak.builder()
			.date(LocalDate.of(2021, 1, 1))
			.build();
		// when
		matchDateStreakRepository.save(matchDateStreak);
		// then
		assertThat(matchDateStreak.getId()).isNotNull();
		assertThat(matchDateStreak.getDate()).isEqualTo(LocalDate.of(2021, 1, 1));
	}

	@Test
	@DisplayName("HistoryId로 해당 날짜에 해당하는 MatchDateStreak를 찾는다.")
	void findByDateAndSearchHistory() {
		// given
		SearchHistory searchHistory = searchHistoryRepository.findById(searchHistoryId).orElseThrow();
		MatchDateStreak matchDateStreak = MatchDateStreak.builder()
			.date(LocalDate.of(2021, 1, 1))
			.build();
		// when
		searchHistory.addMatchDateStreak(matchDateStreak);
		matchDateStreakRepository.save(matchDateStreak);
		// then
		MatchDateStreak found = matchDateStreakRepository.findByDateAndSearchHistory(LocalDate.of(2021, 1, 1),
				searchHistoryId)
			.orElseThrow();
		assertThat(found.getDate()).isEqualTo(LocalDate.of(2021, 1, 1));
		assertThat(found.getSearchHistory().getId()).isEqualTo(searchHistoryId);
		assertThat(found.getSearchHistory().getUser().getId()).isEqualTo(userId);
	}

	@Test
	@DisplayName("HistoryId로 해당 날짜에 해당하는 MatchDateStreak를 찾지 못한다.")
	void findByDateAndSearchHistoryFail() {
		// given
		SearchHistory searchHistory = searchHistoryRepository.findById(searchHistoryId).orElseThrow();
		MatchDateStreak matchDateStreak = MatchDateStreak.builder()
			.date(LocalDate.of(2021, 1, 1))
			.build();
		// when
		searchHistory.addMatchDateStreak(matchDateStreak);
		matchDateStreakRepository.save(matchDateStreak);
		// then
		assertThat(matchDateStreakRepository.findByDateAndSearchHistory(LocalDate.of(2021, 1, 2), searchHistoryId))
			.isEmpty();
	}

}
