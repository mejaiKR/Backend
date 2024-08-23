package mejai.mejaigg.summoner.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
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
import mejai.mejaigg.global.util.YearMonthToEpochUtil;
import mejai.mejaigg.matchstreak.domain.MatchStreak;
import mejai.mejaigg.matchstreak.repository.MatchStreakRepository;
import mejai.mejaigg.riot.service.RiotService;
import mejai.mejaigg.searchhistory.domain.SearchHistory;
import mejai.mejaigg.searchhistory.repository.SearchHistoryRepository;
import mejai.mejaigg.summoner.domain.Summoner;
import mejai.mejaigg.summoner.dto.request.UserStreakRequest;
import mejai.mejaigg.summoner.dto.response.UserStreakDto;
import mejai.mejaigg.summoner.repository.SummonerRepository;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserStreakService {
	private final SummonerRepository userRepository;
	private final SearchHistoryRepository searchHistoryRepository;
	private final MatchStreakRepository matchStreakRepository;
	private final RiotService riotService;

	@Value("${variables.resourceURL:https://ddragon.leagueoflegends.com/cdn/11.16.1/img/profileicon/}")
	private String resourceURL;

	@Transactional(readOnly = false)
	public Optional<List<UserStreakDto>> getUserMonthStreak(UserStreakRequest request) {
		int year = request.getYear();
		int month = request.getMonth();
		Summoner user = userRepository.findBySummonerNameAndTagLineAllIgnoreCase(request.getId(), request.getTag())
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "소환사를 찾을 수 없습니다."));
		YearMonth date = YearMonth.of(year, month);
		SearchHistory history = getHistory(user, date);
		if ((history.isDone() && Duration.between(history.getUpdatedAt(), LocalDateTime.now()).toHours() < 2)
			|| isEmptyHistory(date, user.getId()))
			return Optional.of(getUserStreakDtoList(history));

		//mathData 저장
		saveStreakData(history, date, user.getId());
		userRepository.save(user);
		return Optional.of(getUserStreakDtoList(history));
	}

	private SearchHistory getHistory(Summoner user, YearMonth date) {
		Optional<SearchHistory> searchHistory = searchHistoryRepository.findBySummonerAndDate(user, date);
		// SearchHistory history = new SearchHistory();
		// if (searchHistory.isEmpty()) {
		// 	history.setYearMonthAndUser(date, user);
		// 	searchHistoryRepository.save(history);
		// } else {
		// 	history = searchHistory.get();
		// }
		return null;
	}

	private List<UserStreakDto> getUserStreakDtoList(SearchHistory history) {
		Set<MatchStreak> matchDateStreaks = history.getSortedMatchDateStreaks();
		List<UserStreakDto> userStreakDtos = new ArrayList<>();
		for (MatchStreak matchDateStreak : matchDateStreaks) {
			UserStreakDto userStreakDto = new UserStreakDto();
			userStreakDto.setByMatchDateStreak(matchDateStreak, resourceURL);
			userStreakDtos.add(userStreakDto);
		}
		return userStreakDtos;
	}

	/**
	 *
	 * @param date 해당 년월
	 * @param puuid 해당 유저의 puuid
	 * @return 라이엇 API를 통해 해당 년월에 해당하는 매치 히스토리를 가져와서 비어있는지 여부를 반환
	 */
	private boolean isEmptyHistory(YearMonth date, String puuid) {
		long startTime = YearMonthToEpochUtil.convertToEpochSeconds(date.atDay(1));
		long endTime = YearMonthToEpochUtil.convertToEpochSeconds(date.atEndOfMonth());
		String[] monthHistories = riotService.getMatchHistoryByPuuid(puuid, startTime, endTime, 0L,
			100); //100개의 매치를 가져옴
		return monthHistories == null || monthHistories.length == 0;
	}

	private void saveStreakData(SearchHistory history, YearMonth dateYM, String puuid) {
		int startDay = history.getLastSuccessDay();
		if (startDay >= dateYM.lengthOfMonth())
			return;

		long startTime = YearMonthToEpochUtil.convertToEpochSeconds(dateYM.atDay(startDay));
		long endTime = YearMonthToEpochUtil.convertToEpochSeconds(dateYM.atEndOfMonth());
		String[] matchHistory = riotService.getMatchHistoryByPuuid(puuid, startTime, endTime, 0L, 100);
		if (matchHistory.length == 0) {
			searchHistoryRepository.updateIsDoneByHistoryId(history.getId(), true);
			return;
		}
		int historyLen = matchHistory.length;
		for (int i = startDay; i < dateYM.lengthOfMonth(); i++) { // TODO: Batch 처리
			if (matchStreakRepository.findByDateAndSearchHistory(dateYM.atDay(i), history.getId()).isPresent())
				continue;
			startTime = YearMonthToEpochUtil.convertToEpochSeconds(dateYM.atDay(i));
			endTime = YearMonthToEpochUtil.convertToEpochSeconds(dateYM.atDay(i + 1));
			try {
				if (historyLen == 0)
					break;

				matchHistory = riotService.getMatchHistoryByPuuid(puuid, startTime, endTime, 0L,
					100); //하루 지난 후 100개의 매치를 가져옴
				if (matchHistory == null || matchHistory.length == 0)
					continue;

				MatchStreak matchDateStreak = MatchStreak.builder()
					.date(dateYM.atDay(i))
					.searchHistory(history)
					.allGameCount(matchHistory.length)
					.build();
				history.addMatchDateStreak(matchDateStreak);
				if (historyLen != 100) {
					historyLen -= matchHistory.length;
				}
				matchStreakRepository.save(matchDateStreak);
			} catch (Exception e) {
				log.error("Http Error" + e.getMessage());
				searchHistoryRepository.updateLastSuccessDateByHistoryId(history.getId(), i);
				return;
			}
		}
		if (!dateYM.equals(LocalDate.now().withDayOfMonth(1))) {
			searchHistoryRepository.updateIsDoneByHistoryId(history.getId(), true);
			searchHistoryRepository.updateLastSuccessDateByHistoryId(history.getId(),
				YearMonthToEpochUtil.getNowDay());
		} else {
			searchHistoryRepository.updateLastSuccessDateByHistoryId(history.getId(),
				31);
		}
	}
}
