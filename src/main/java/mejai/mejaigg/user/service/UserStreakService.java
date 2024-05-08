package mejai.mejaigg.user.service;

import java.sql.Date;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mejai.mejaigg.common.util.YearMonthToEpochUtil;
import mejai.mejaigg.match.repository.MatchRepository;
import mejai.mejaigg.matchdatestreak.entity.MatchDateStreak;
import mejai.mejaigg.matchdatestreak.repository.MatchDateStreakRepository;
import mejai.mejaigg.riot.service.RiotService;
import mejai.mejaigg.searchhistory.entity.SearchHistory;
import mejai.mejaigg.searchhistory.repository.SearchHistoryRepository;
import mejai.mejaigg.user.dto.request.UserStreakRequest;
import mejai.mejaigg.user.dto.response.UserStreakDto;
import mejai.mejaigg.user.entity.User;
import mejai.mejaigg.user.repository.UserRepository;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserStreakService {
	private final UserRepository userRepository;
	private final SearchHistoryRepository searchHistoryRepository;
	private final MatchDateStreakRepository matchDateStreakRepository;
	private final RiotService riotService;
	private final MatchRepository matchRepository;

	@Value("${variables.resourceURL:https://ddragon.leagueoflegends.com/cdn/11.16.1/img/profileicon/}")
	private String resourceURL;

	@Transactional(readOnly = false)
	public Optional<List<UserStreakDto>> getUserMonthStreak(UserStreakRequest request) {
		int year = request.getYear();
		int month = request.getMonth();
		User user = userRepository.findBySummonerNameAndTagLineAllIgnoreCase(request.getId(), request.getTag())
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "소환사를 찾을 수 없습니다."));
		String dateYM = String.format("%d-%02d", year, month);
		SearchHistory history = getHistory(user, dateYM);
		if (history.isDone() || isEmptyHistory(dateYM, user.getPuuid()) ||
			(history.getUpdatedAt() != null && Duration.between(history.getUpdatedAt(), LocalDateTime.now()).toHours() < 2)){
			return Optional.of(getUserStreakDtoList(history));
		}
		//mathData 저장
		saveStreakData(history, dateYM, user.getPuuid());
		userRepository.save(user);
		return Optional.of(getUserStreakDtoList(history));
	}

	private SearchHistory getHistory(User user, String dateYM) {
		Optional<SearchHistory> searchHistory = searchHistoryRepository.findByUserAndYearMonth(user, dateYM);
		SearchHistory history = new SearchHistory();
		if (searchHistory.isEmpty()) {
			history.setYearMonthAndUser(dateYM, user);
			searchHistoryRepository.save(history);
		} else {
			history = searchHistory.get();
		}
		return history;
	}

	private List<UserStreakDto> getUserStreakDtoList(SearchHistory history) {
		Set<MatchDateStreak> matchDateStreaks = history.getSortedMatchDateStreaks();
		List<UserStreakDto> userStreakDtos = new ArrayList<>();
		for (MatchDateStreak matchDateStreak : matchDateStreaks) {
			UserStreakDto userStreakDto = new UserStreakDto();
			userStreakDto.setByMatchDateStreak(matchDateStreak, resourceURL);
			userStreakDtos.add(userStreakDto);
		}
		return userStreakDtos;
	}

	private boolean isEmptyHistory(String dateYM, String puuid) {
		long startTime = YearMonthToEpochUtil.convertToEpochSeconds(dateYM);
		long endTime = YearMonthToEpochUtil.addMonthEpochSecond(dateYM, 1);
		if (dateYM.equals(YearMonthToEpochUtil.getNowYearMonth())) { // 현재 월에 해당하는 경우 현재 날짜까지만 가져옴
			endTime = YearMonthToEpochUtil.getNowEpochSecond();
		}
		String[] monthHistories = riotService.getMatchHistoryByPuuid(puuid, startTime, endTime, 0L,
			100); //100개의 매치를 가져옴
		return monthHistories == null || monthHistories.length == 0;
	}

	private void saveStreakData(SearchHistory history, String dateYM, String puuid) {
		int startDay = history.getLastSuccessDay();
		int endDay = YearMonthToEpochUtil.getDayWithYearMonth(dateYM);
		if (startDay >= endDay) {
			return;
		}

		long startTime = YearMonthToEpochUtil.addDayEpochSecond(dateYM, startDay);
		long endTime = YearMonthToEpochUtil.addDayEpochSecond(dateYM, endDay);
		String[] matchHistory = riotService.getMatchHistoryByPuuid(puuid, startTime, endTime, 0L, 100);
		if (matchHistory.length == 0) {
			searchHistoryRepository.updateIsDoneByHistoryId(history.getHistoryId(), true);
			return;
		}
		int historyLen = matchHistory.length;
		for (int i = startDay; i < endDay; i++) { // 하루씩 데이터를 가져옴
			if (matchDateStreakRepository.findByDateAndSearchHistory(
				new Date(YearMonthToEpochUtil.addDayEpochSecond(dateYM, i)),
				history.getHistoryId()).isPresent()) {
				continue;
			}
			startTime = YearMonthToEpochUtil.addDayEpochSecond(dateYM, i);
			endTime = YearMonthToEpochUtil.addDayEpochSecond(dateYM, i + 1);
			try {
				if (historyLen == 0) {
					break;
				}
				matchHistory = riotService.getMatchHistoryByPuuid(puuid, startTime, endTime, 0L,
					100); //하루 지난 후 100개의 매치를 가져옴
				if (matchHistory == null || matchHistory.length == 0) {
					continue;
				}
				MatchDateStreak matchDateStreak = new MatchDateStreak();
				Date date = new Date(YearMonthToEpochUtil.addDayEpochSecond(dateYM, i) * 1000L);
				matchDateStreak.setDate(date);
				matchDateStreak.setAllGameCount(matchHistory.length);
				history.addMatchDateStreak(matchDateStreak);
				if (historyLen != 100) {
					historyLen -= matchHistory.length;
				}
				// for (String matchId : matchHistory) {
				// 	Optional<Match> optionalMatch = matchRepository.findById(matchId);
				// 	Match match = new Match(matchId, false);
				// 	if (optionalMatch.isPresent()) {
				// 		match = optionalMatch.get();
				// 	}
				// 	matchDateStreak.addMatch(match);
				// }
				matchDateStreakRepository.save(matchDateStreak);
			} catch (Exception e) {
				log.error("Http Error" + e.getMessage());
				searchHistoryRepository.updateLastSuccessDateByHistoryId(history.getHistoryId(), i);
				return;
			}
		}
		if (!dateYM.equals(YearMonthToEpochUtil.getNowYearMonth())) {
			searchHistoryRepository.updateIsDoneByHistoryId(history.getHistoryId(), true);
			searchHistoryRepository.updateLastSuccessDateByHistoryId(history.getHistoryId(),
				YearMonthToEpochUtil.getNowDay());
		} else {
			searchHistoryRepository.updateLastSuccessDateByHistoryId(history.getHistoryId(),
				31);
		}
	}
}
