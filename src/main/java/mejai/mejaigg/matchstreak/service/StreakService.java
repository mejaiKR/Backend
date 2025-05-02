package mejai.mejaigg.matchstreak.service;

import java.time.LocalDate;
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
			.orElseGet(() -> {
				SearchHistory newHistory = initializeSearchHistory(summoner, yearMonth);
				return searchHistoryRepository.save(newHistory);
			});

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
		long endTime = 0;
		if (startDay == yearMonth.lengthOfMonth())
			endTime = YearMonthToEpochUtil.convertToEpochSeconds(yearMonth.atDay(startDay).plusDays(1));
		else
			endTime = YearMonthToEpochUtil.convertToEpochSeconds(yearMonth.atDay(endDay));
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

	private void updateStreakData(SearchHistory history, YearMonth ym, String puuid) {
		int start = history.getLastSuccessDay();
		for (int i = Math.max(1, start); i <= ym.lengthOfMonth(); i++) {
			try {
				String[] days = getMonthHistories(ym, puuid, i, i + 1);
				if (days == null || days.length == 0) { // 게임을 안 한 날도 lastSuccessDay 올림
					history.setLastSuccessDay(i);
					continue;
				}

				// 이미 있는지 확인 & 없으면 생성
				LocalDate day = ym.atDay(i);
				MatchStreak ms = matchStreakRepository
					.findBySearchHistoryAndDate(history, day)
					.orElseGet(() -> {
						MatchStreak tm = MatchStreak.builder()
							.date(day)
							.allGameCount(0)
							.build();
						history.addMatchDateStreak(tm);
						return tm;
					});
				ms.setAllGameCount(days.length);
				history.setLastSuccessDay(i);
			} catch (Exception e) {
				log.error("스트릭 저장중에 에러 발생" + e.getMessage());
				searchHistoryRepository.save(history);
				return;
			}
		}

		history.setDone(!ym.equals(YearMonth.now()));
		history.setUpdatedAt(LocalDateTime.now());
		searchHistoryRepository.save(history);
	}
}
