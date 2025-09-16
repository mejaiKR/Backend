package kr.mejai.project.global.util;

import java.time.YearMonth;

import kr.mejai.project.domain.searchhistory.domain.SearchHistory;
import kr.mejai.project.domain.summoner.domain.Summoner;

public class TestUtil {
	public static Summoner createTestUser() {
		return Summoner.builder()
			.puuid("puuid1")
			.tagLine("tagLine1")
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
