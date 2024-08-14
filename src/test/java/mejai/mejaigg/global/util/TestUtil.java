package mejai.mejaigg.global.util;

import java.time.YearMonth;

import mejai.mejaigg.searchhistory.entity.SearchHistory;
import mejai.mejaigg.summoner.entity.User;

public class TestUtil {
	public static User createTestUser() {
		return User.builder().id("puuid1")
			.accountId("accountId1")
			.tagLine("tagLine1")
			.summonerId("summonerId1")
			.summonerLevel(1L)
			.summonerName("summonerName1")
			.revisionDate(1L)
			.profileIconId(1)
			.build();
	}

	public static SearchHistory createTestSearchHistory(User user, YearMonth yearMonth) {
		SearchHistory searchHistory = SearchHistory.builder().build();
		searchHistory.setYearMonthAndUser(yearMonth, user);
		return searchHistory;
	}
}
