package mejai.mejaigg.matchstreak.service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mejai.mejaigg.global.config.RiotProperties;
import mejai.mejaigg.global.exception.RestApiException;
import mejai.mejaigg.global.util.YearMonthToEpochUtil;
import mejai.mejaigg.matchstreak.domain.MatchStreak;
import mejai.mejaigg.matchstreak.exception.StreakErrorCode;
import mejai.mejaigg.matchstreak.repository.MatchStreakRepository;
import mejai.mejaigg.riot.service.RiotService;
import mejai.mejaigg.searchhistory.domain.SearchHistory;
import mejai.mejaigg.searchhistory.repository.SearchHistoryRepository;
import mejai.mejaigg.summoner.domain.Summoner;
import mejai.mejaigg.summoner.dto.response.RenewalStatusResponse;
import mejai.mejaigg.summoner.dto.response.SummonerStreak;
import mejai.mejaigg.summoner.dto.response.SummonerStreakResponse;
import mejai.mejaigg.summoner.service.SummonerService;

@Service
@Slf4j
@RequiredArgsConstructor
public class StreakService {
	private final SummonerService summonerService;
	private final SearchHistoryRepository searchHistoryRepository;
	private final MatchStreakRepository matchStreakRepository;
	private final RiotService riotService;
	private final RiotProperties riotProperties;

	/**
	 * 소환사의 게임 횟수 및 승패 조회 (단순 조회만 수행)
	 *
	 * @param summonerName 소환사 아이디
	 * @param tag  태그
	 * @param year 년도
	 * @param month 월
	 * @return 소환사의 게임 횟수 및 승패 조회 결과
	 */
	public SummonerStreakResponse getStreak(String summonerName, String tag, int year, int month) {
		Summoner summoner = summonerService.findOrCreateSummoner(summonerName, tag);
		YearMonth date = YearMonth.of(year, month);
		SearchHistory history = searchHistoryRepository.findBySummonerAndDate(summoner, date)
			.orElseThrow(() -> new RestApiException(StreakErrorCode.STREAK_NOT_FOUND));

		return getUserStreakDtoList(history);
	}

	/**
	 * 소환사의 게임 횟수 및 승패 업데이트 (라이엇 API를 통해 매치 히스토리를 가져와서 업데이트)
	 *
	 * @param summonerName 소환사 아이디
	 * @param tag  태그
	 * @param year 년도
	 * @param month 월
	 * @return 소환사의 게임 횟수 및 승패 업데이트 결과
	 */
	@Transactional
	public void renewalStreak(String summonerName, String tag, int year, int month) {
		Summoner summoner = summonerService.findOrCreateSummoner(summonerName, tag);
		YearMonth yearMonth = YearMonth.of(year, month);
		SearchHistory history = searchHistoryRepository.findBySummonerAndDate(summoner, yearMonth)
			.orElseGet(() -> initializeSearchHistory(summoner, yearMonth));

		if (history.getUpdatedAt().plusHours(2).isAfter(LocalDateTime.now())) {
			log.info("스트릭 업데이트를 한지 2시간 밖에 지나지 않았습니다");
			return;
		}

		updateStreakData(history, yearMonth, summoner.getPuuid());
	}

	public RenewalStatusResponse getStreakRenewalStatus(String summonerName, String tag, int year, int month) {
		Summoner summoner = summonerService.findOrCreateSummoner(summonerName, tag);
		YearMonth yearMonth = YearMonth.of(year, month);
		SearchHistory history = searchHistoryRepository.findBySummonerAndDate(summoner, yearMonth)
			.orElse(null);

		if (history == null) {
			return new RenewalStatusResponse(LocalDateTime.of(1900, 1, 1, 0, 0, 0, 1000));
		}

		return new RenewalStatusResponse(history.getUpdatedAt());
	}

	private SearchHistory initializeSearchHistory(Summoner summoner, YearMonth yearMonth) {
		SearchHistory history = SearchHistory.builder()
			.summoner(summoner)
			.date(yearMonth)
			.build();
		updateStreakData(history, yearMonth, summoner.getPuuid());
		return history;
	}

	private String[] getMonthHistories(YearMonth yearMonth, String puuid, int startDay, int endDay) {
		long startTime = YearMonthToEpochUtil.convertToEpochSeconds(yearMonth.atDay(startDay));
		long endTime = YearMonthToEpochUtil.convertToEpochSeconds(yearMonth.atDay(endDay));

		return riotService.getMatchHistoryByPuuid(puuid, startTime, endTime, 0L, 100);
	}

	private SummonerStreakResponse getUserStreakDtoList(SearchHistory history) {
		Set<MatchStreak> matchDateStreaks = history.getSortedMatchDateStreaks();

		List<SummonerStreak> summonerStreak = new ArrayList<>();
		for (MatchStreak matchDateStreak : matchDateStreaks) {
			summonerStreak.add(new SummonerStreak(matchDateStreak, riotProperties.getResourceUrl()));
		}

		return new SummonerStreakResponse(summonerStreak, history.getUpdatedAt());
	}

	// Todo: 갱신시 에러 발생하는 케이스 확인필요
	private void updateStreakData(SearchHistory history, YearMonth dateYM, String puuid) {
		int startDay = history.getLastSuccessDay();
		if (startDay >= dateYM.lengthOfMonth()) {
			history.setDone(true);
			history.setUpdatedAt(LocalDateTime.now());
			searchHistoryRepository.save(history);
			return;
		}
		if (startDay == 0)
			startDay = 1;
		String[] monthHistories = getMonthHistories(dateYM, puuid, startDay, dateYM.lengthOfMonth());
		int historyLen = monthHistories.length;
		for (int i = startDay; i < dateYM.lengthOfMonth(); i++) {
			try {
				if (historyLen <= 0)
					break;
				monthHistories = getMonthHistories(dateYM, puuid, i, i + 1);
				if (monthHistories == null || monthHistories.length == 0)
					continue;
				MatchStreak matchDateStreak = MatchStreak.builder()
					.date(dateYM.atDay(i))
					.searchHistory(history)
					.allGameCount(monthHistories.length)
					.build();
				history.addMatchDateStreak(matchDateStreak);
				if (historyLen != 100)
					historyLen -= monthHistories.length;
				matchStreakRepository.save(matchDateStreak);
			} catch (Exception e) {
				log.error("스트릭 저장중에 에러 발생" + e.getMessage());
				history.setLastSuccessDay(i);
				searchHistoryRepository.save(history);
				return;
			}
		}

		if (dateYM.equals(YearMonth.now())) { // 만약 이번달인 경우
			history.setLastSuccessDay(YearMonthToEpochUtil.getNowDay());
		} else {
			history.setLastSuccessDay(dateYM.lengthOfMonth());
			history.setDone(true);
		}
		history.setUpdatedAt(LocalDateTime.now());
		searchHistoryRepository.save(history);
	}
}
