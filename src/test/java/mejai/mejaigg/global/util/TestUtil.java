package mejai.mejaigg.global.util;

import java.time.YearMonth;

import mejai.mejaigg.searchhistory.domain.SearchHistory;
import mejai.mejaigg.summoner.domain.Summoner;

public class TestUtil {
	public static Summoner createTestUser() {
		return Summoner.builder()
			.puuid("puuid1")
			.accountId("accountId1")
			.tagLine("tagLine1")
			.summonerId("summonerId1")
			.summonerLevel(1L)
			.summonerName("summonerName1")
			.revisionDate(1L)
			.profileIconId(1)
			.build();
	}

	public static SearchHistory createTestSearchHistory(Summoner summoner, YearMonth yearMonth) {
		SearchHistory searchHistory = SearchHistory.builder().build();
		searchHistory.setYearMonthAndUser(yearMonth, summoner);
		return searchHistory;
	}
}
