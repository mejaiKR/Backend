package mejai.mejaigg.service;

import java.sql.Date;
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
import mejai.mejaigg.common.YearMonthToEpochUtil;
import mejai.mejaigg.domain.Match;
import mejai.mejaigg.domain.MatchDateStreak;
import mejai.mejaigg.domain.Rank;
import mejai.mejaigg.domain.SearchHistory;
import mejai.mejaigg.domain.User;
import mejai.mejaigg.dto.response.UserProfileDto;
import mejai.mejaigg.dto.response.UserStreakDto;
import mejai.mejaigg.dto.riot.AccountDto;
import mejai.mejaigg.dto.riot.RankDto;
import mejai.mejaigg.dto.riot.SummonerDto;
import mejai.mejaigg.mapper.RankMapper;
import mejai.mejaigg.mapper.UserMapper;
import mejai.mejaigg.repository.GameRepository;
import mejai.mejaigg.repository.MatchDateStreakRepository;
import mejai.mejaigg.repository.MatchRepository;
import mejai.mejaigg.repository.RankRepository;
import mejai.mejaigg.repository.SearchHistoryRepository;
import mejai.mejaigg.repository.UserGameStatRepository;
import mejai.mejaigg.repository.UserRepository;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

	private final ApiService apiService;
	private final UserRepository userRepository;
	private final RankRepository rankRepository;
	private final SearchHistoryRepository searchHistoryRepository;
	private final UserGameStatRepository userGameStatRepository;
	private final GameRepository gameRepository;
	private final MatchDateStreakRepository matchDateStreakRepository;
	private final MatchRepository matchRepository;

	@Value("${variables.resourceURL:https://ddragon.leagueoflegends.com/cdn/11.16.1/img/profileicon/}")
	private String resourceURL;

	//TODO : api 콜을 실패하는 경우 고려해야함
	//TODO : 비동기 레포지토리 방식 적용
	//TODO : 시즌 바꼈을때 추가하는 로직 필요하다.
	//처음으로 요청이 들어왔을 때 호출되는 서비스
	@Transactional(readOnly = false)
	public String setUserProfile(String name, String tag) { //처음 콜 할 때 세팅 되는 함수
		Mono<AccountDto> account = apiService.getAccountByNameAndTag(name, tag);
		AccountDto accountDto = account.block();
		Mono<SummonerDto> summoner = apiService.getSummonerByPuuid(accountDto.getPuuid());
		SummonerDto summonerDto = summoner.block();
		User user = UserMapper.INSTANCE.toUserEntity(accountDto, summonerDto);

		Mono<Set<RankDto>> rankBySummonerId = apiService.getRankBySummonerId(summonerDto.getId());
		RankDto rankDto = rankBySummonerId.block().stream().findFirst().orElse(null);
		Rank rank = new Rank();

		if (rankDto != null) { //랭크가 없는 경우에는 배열이 비었다. (언랭 유저)
			rank = RankMapper.INSTANCE.toRankEntity(rankDto);
		} else {
			rank.setUnRanked();
		}
		rank.setUser(user);
		rankRepository.save(rank);
		user.setRank(rank);
		userRepository.save(user);
		return user.getPuuid();
	}

	@Transactional(readOnly = false)
	public String updateUserProfile(User user) {
		Mono<SummonerDto> summoner = apiService.getSummonerByPuuid(user.getPuuid());
		SummonerDto summonerDto = summoner.block();
		Mono<Set<RankDto>> rankBySummonerId = apiService.getRankBySummonerId(summonerDto.getId());
		RankDto rankDto = rankBySummonerId.block().stream().findFirst().orElse(null);
		user.updateBySummonerDto(summonerDto);
		user.getRank().updateByRankDto(rankDto);
		return user.getPuuid();
	}

	@Transactional(readOnly = false)
	public List<UserStreakDto> getUserMonthStreak(String puuid, int year, int month) {
		Long start = 0L;
		String dateYM = String.format("%d-%02d", year, month);
		long startTime = YearMonthToEpochUtil.convertToEpochSeconds(year, month);
		long endTime;
		Optional<User> userOptional = userRepository.findById(puuid);
		if (userOptional.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "summoner not found");
		}
		User user = userOptional.get();
		Optional<SearchHistory> searchHistory = searchHistoryRepository.findByUserAndYearMonth(user, dateYM);
		SearchHistory history = new SearchHistory();
		List<UserStreakDto> userStreakDtos = new ArrayList<>();
		if (searchHistory.isEmpty()) { //처음으로 호출하는 경우
			history.setYearMonthAndUser(dateYM, user);
			log.info("history {}", history);
			log.info("user {}", user);
			searchHistoryRepository.save(history);
		} else {
			history = searchHistory.get();
			if (history.isDone()) {
				return getUserStreakDtoList(history);
			}
		}
		//이제 데이터 저장 하기
		try {
			boolean isNow = false;
			int days = YearMonthToEpochUtil.getDayWithYearMonth(dateYM);
			int nowDate = YearMonthToEpochUtil.getNowEpochSecond();
			String nowYearMonth = YearMonthToEpochUtil.getNowYearMonth();
			if (dateYM.equals(nowYearMonth)) { // 현재 월에 해당하는 경우 현재 날짜까지만 가져옴
				endTime = nowDate;
				days = YearMonthToEpochUtil.getNowDay();
				isNow = true;
				System.out.println("현재 날짜에 도달!!");
			} else {
				endTime = YearMonthToEpochUtil.addMonthEpochSecond(dateYM, 1);
			}
			// 	//하루 단위로 api 호출해서 저장
			Mono<String[]> monthHistories = apiService.getMatchHistoryByPuuid(puuid, startTime, endTime, start,
				100); //100개의 매치를 가져옴
			String[] monthIds = monthHistories.block();
			if (monthIds == null) {
				return userStreakDtos;
			}
			System.out.println("매치 가져와! 길이는 ?" + monthIds.length);

			// if (monthIds.length < days) { // 갯수가 하루씩 부르는 것보다 더 적은 경우 하나씩 데이터를 가져옴
			// 	for (String matchId : monthIds) {
			// 		Match match = new Match(matchId, true);
			// 		Mono<MatchDto> matchData = apiService.getMatchDtoByMatchId(matchId);
			// 		MatchDto matchDto = matchData.block();
			//
			// 		InfoDto info = matchDto.getInfo();
			// 		Game gameEntity = GameMapper.INSTANCE.toGameEntity(info, matchId);
			// 		ParticipantDto[] participants = matchDto.getInfo().getParticipants();
			// 		for (ParticipantDto participant : participants) {
			// 			UserGameStat userGameStat = new UserGameStat();
			// 			userGameStat.setByParticipantDto(participant);
			// 			userGameStat.setGame(gameEntity);
			// 			gameEntity.addGameStat(userGameStat);
			// 		}
			// 		match.setGame(gameEntity);
			// 		gameRepository.save(gameEntity);
			//
			// 		String yearMonth = YearMonthToEpochUtil.convertToYearMonthDay(info.getGameCreation());
			// 		Date matchDate = new Date(info.getGameCreation());
			// 		Optional<MatchDateStreak> streakOptional = matchDateStreakRepository.findByDateAndSearchHistory(
			// 			matchDate, history.getHistoryId());
			// 		if (streakOptional.isEmpty()) {
			// 			MatchDateStreak matchDateStreak = new MatchDateStreak();
			// 			matchDateStreak.setDate(matchDate);
			// 			history.addMatchDateStreak(matchDateStreak);
			//
			// 			matchDateStreakRepository.save(matchDateStreak);
			// 			// gameRepository.save(gameEntity);
			// 			matchRepository.save(match);
			// 			// searchHistoryRepository.save(history);
			// 			// gameRepository.save(gameEntity);
			// 		} else {
			// 			MatchDateStreak matchDateStreak = streakOptional.get();
			// 			history.addMatchDateStreak(matchDateStreak);
			// 			match.setGame(gameEntity);
			// 			matchDateStreakRepository.save(matchDateStreak);
			// 			matchRepository.save(match);
			// 			gameRepository.save(gameEntity);
			// 			userGameStatRepository.saveAll(gameEntity.getGameStats());
			// 		}
			// 		match.setGame(gameEntity);
			// 		gameRepository.save(gameEntity);
			// 	}
			// }
			// } else {

			storeDailyForMonth(puuid, days, dateYM, history, start);
			if (!isNow)
				searchHistoryRepository.updateIsDoneByHistoryId(history.getHistoryId(), true);
			userRepository.save(user);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "summoner not found");
		}
		return getUserStreakDtoList(history);
	}

	private List<UserStreakDto> getUserStreakDtoList(SearchHistory history) {
		Set<MatchDateStreak> matchDateStreaks = history.getSortedMatchDateStreaks();
		List<UserStreakDto> userStreakDtos = new ArrayList<>();
		for (MatchDateStreak matchDateStreak : matchDateStreaks) {
			UserStreakDto userStreakDto = new UserStreakDto();
			userStreakDto.setByMatchDateStreak(matchDateStreak);
			userStreakDtos.add(userStreakDto);
		}
		return userStreakDtos;
	}

	private void storeDailyForMonth(String puuid, int days, String dateYM, SearchHistory history, Long start) {
		long endTime;
		long startTime;
		for (int i = 0; i < days; i++) { // 하루씩 데이터를 가져옴
			if (matchDateStreakRepository.findByDateAndSearchHistory(
				new Date(YearMonthToEpochUtil.addDayEpochSecond(dateYM, i)),
				history.getHistoryId()).isPresent()) {
				continue;
			}
			startTime = YearMonthToEpochUtil.addDayEpochSecond(dateYM, i);
			endTime = YearMonthToEpochUtil.addDayEpochSecond(dateYM, i + 1);
			Mono<String[]> matchHistoryByPuuid = apiService.getMatchHistoryByPuuid(puuid, startTime, endTime, start,
				100); //100개의 매치를 가져옴
			String[] matchHistory = matchHistoryByPuuid.block();
			if (matchHistory == null || matchHistory.length == 0) {
				continue;
			}
			MatchDateStreak matchDateStreak = new MatchDateStreak();
			Date date = new Date(YearMonthToEpochUtil.addDayEpochSecond(dateYM, i) * 1000L);
			System.out.println("!!!!!!!!!date : " + date);
			matchDateStreak.setDate(date);
			history.addMatchDateStreak(matchDateStreak);
			for (String matchId : matchHistory) {
				Match match = new Match(matchId, false);
				matchDateStreak.addMatch(match);
			}
			matchDateStreakRepository.save(matchDateStreak);
		}
	}

	@Transactional
	public String getPuuidByNameTag(String name, String tag) {
		Optional<User> userOptional = userRepository.findBySummonerNameAndTagLineAllIgnoreCase(name, tag);
		if (userOptional.isEmpty()) {
			return setUserProfile(name, tag);
		} else {
			return userOptional.get().getPuuid();
		}
	}

	@Transactional
	public UserProfileDto getUserProfileByNameTag(String name, String tag) {
		Optional<User> userOptional = userRepository.findBySummonerNameAndTagLineAllIgnoreCase(name, tag);
		if (userOptional.isEmpty()) {
			try {
				String puuid = setUserProfile(name, tag);
				if (puuid == null) {
					throw new ResponseStatusException(HttpStatus.NOT_FOUND, "summoner not found");
				}
				userOptional = userRepository.findById(puuid);
			} catch (Exception e) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "summoner not found");
			}
		} else {
			updateUserProfile(userOptional.get());
		}
		UserProfileDto userProfileDto = new UserProfileDto();
		if (userOptional.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "summoner not found");
		}
		User user = userOptional.get();
		userProfileDto.setByUser(user, resourceURL);
		return userProfileDto;
	}
}
