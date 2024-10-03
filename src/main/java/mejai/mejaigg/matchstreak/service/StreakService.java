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
import mejai.mejaigg.summoner.dto.request.UserStreakRequest;
import mejai.mejaigg.summoner.dto.response.UserStreakDto;
import mejai.mejaigg.summoner.exception.SummonerErrorCode;
import mejai.mejaigg.summoner.repository.SummonerRepository;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StreakService {
	private final SummonerRepository userRepository;
	private final SearchHistoryRepository searchHistoryRepository;
	private final MatchStreakRepository matchStreakRepository;
	private final RiotService riotService;
	private final RiotProperties riotProperties;

	/**
	 * 소환사의 게임 횟수 및 승패 조회 (단순 조회만 수행)
	 * @param request 소환사 아이디, 태그, 년도, 월
	 * @return 소환사의 게임 횟수 및 승패 조회 결과
	 */
	public List<UserStreakDto> getStreak(UserStreakRequest request) {
		int year = request.getYear();
		int month = request.getMonth();
		Summoner user = userRepository.findBySummonerNameAndTagLineAllIgnoreCase(request.getId(), request.getTag())
			.orElseThrow(() -> new RestApiException(SummonerErrorCode.SUMMONER_NOT_FOUND));
		YearMonth date = YearMonth.of(year, month);
		SearchHistory history = searchHistoryRepository.findBySummonerAndDate(user, date)
			.orElseThrow(() -> new RestApiException(StreakErrorCode.STREAK_NOT_FOUND));
		return getUserStreakDtoList(history);
	}

	/**
	 * 소환사의 게임 횟수 및 승패 업데이트 (라이엇 API를 통해 매치 히스토리를 가져와서 업데이트)
	 * @param request 소환사 아이디, 태그, 년도, 월
	 * @return 소환사의 게임 횟수 및 승패 업데이트 결과
	 */
	@Transactional(readOnly = false)
	public List<UserStreakDto> refreshStreak(UserStreakRequest request) {
		YearMonth yearMonth = YearMonth.of(request.getYear(), request.getMonth());
		Summoner user = userRepository.findBySummonerNameAndTagLineAllIgnoreCase(request.getId(), request.getTag())
			.orElseThrow(() -> new RestApiException(SummonerErrorCode.SUMMONER_NOT_FOUND));
		SearchHistory history = searchHistoryRepository.findBySummonerAndDate(user, yearMonth).orElse(null);
		if (history == null) {
			history = SearchHistory.builder()
				.summoner(user)
				.date(yearMonth)
				.build();
			searchHistoryRepository.save(history);
		} else {
			//이미 검색이 끝났거나, 이번달 기록인데 아직 2시간이 안 지났거나
			if (history.isDone()
				|| (yearMonth.equals(YearMonth.now()) && history.getUpdatedAt()
				.plusHours(2)
				.isBefore(LocalDateTime.now()))) {
				log.info("스트릭 업데이트를 한지 2시간 밖에 지나지 않았습니다");
				history.setUpdatedAt(LocalDateTime.now());
				searchHistoryRepository.save(history);
				return getUserStreakDtoList(history);
			}
			String[] monthHistories = getMonthHistories(yearMonth, user.getPuuid(), 1, yearMonth.lengthOfMonth());
			if (monthHistories == null || monthHistories.length == 0) { // 빈 히스토리인 경우
				searchHistoryRepository.updateIsDoneByHistoryId(history.getId(), true);
				return getUserStreakDtoList(history);
			}
		}
		updateStreakData(history, yearMonth, user.getPuuid());
		// //mathData 저장
		return getUserStreakDtoList(history);
	}

	private String[] getMonthHistories(YearMonth yearMonth, String puuid, int startDay, int endDay) {
		long startTime = YearMonthToEpochUtil.convertToEpochSeconds(yearMonth.atDay(startDay));
		long endTime = YearMonthToEpochUtil.convertToEpochSeconds(yearMonth.atDay(endDay));
		return riotService.getMatchHistoryByPuuid(puuid, startTime, endTime, 0L, 100);
	}

	private List<UserStreakDto> getUserStreakDtoList(SearchHistory history) {
		Set<MatchStreak> matchDateStreaks = history.getSortedMatchDateStreaks();
		List<UserStreakDto> userStreakDtos = new ArrayList<>();
		for (MatchStreak matchDateStreak : matchDateStreaks) {
			UserStreakDto userStreakDto = new UserStreakDto();
			userStreakDto.setByMatchDateStreak(matchDateStreak, riotProperties.getResourceUrl());
			userStreakDtos.add(userStreakDto);
		}
		return userStreakDtos;
	}

	private void updateStreakData(SearchHistory history, YearMonth dateYM, String puuid) {
		int startDay = history.getLastSuccessDay();
		history.setUpdatedAt(LocalDateTime.now());
		if (startDay >= dateYM.lengthOfMonth()) {
			searchHistoryRepository.updateIsDoneByHistoryId(history.getId(), true);
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
				log.error("Http Error: " + e.getMessage());
				searchHistoryRepository.updateLastSuccessDateByHistoryId(history.getId(), i);
				return;
			}
		}

		if (dateYM.equals(YearMonth.now())) { // 만약 이번달인 경우
			searchHistoryRepository.updateLastSuccessDateByHistoryId(history.getId(), YearMonthToEpochUtil.getNowDay());
		} else {
			searchHistoryRepository.updateLastSuccessDateByHistoryId(history.getId(),
				31);
			searchHistoryRepository.updateIsDoneByHistoryId(history.getId(), true);
		}
		searchHistoryRepository.save(history);
	}
}
