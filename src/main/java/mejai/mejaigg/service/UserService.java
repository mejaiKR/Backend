package mejai.mejaigg.service;

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
import mejai.mejaigg.domain.Rank;
import mejai.mejaigg.domain.User;
import mejai.mejaigg.dto.response.UserProfileDto;
import mejai.mejaigg.dto.response.UserStreakDto;
import mejai.mejaigg.dto.riot.AccountDto;
import mejai.mejaigg.dto.riot.RankDto;
import mejai.mejaigg.dto.riot.SummonerDto;
import mejai.mejaigg.mapper.RankMapper;
import mejai.mejaigg.mapper.UserMapper;
import mejai.mejaigg.repository.RankRepository;
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

	public Set<UserStreakDto> getUserMonthStreak(String puuid, String dateYM) {
		setUserStrakOneMonth(puuid, dateYM);
		return null;
	}

	private void setUserStrakOneMonth(String puuid, String dateYM) {
		Long start = 0L;
		//TODO : dateYM 포멧 맞는지 확인
		Long startTime = YearMonthToEpochUtil.convertToEpochSeconds(dateYM);
		Optional<User> userOptional = userRepository.findById(puuid);
		if (userOptional.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "summoner not found");
		}
		User user = userOptional.get();
		try {
			int days = YearMonthToEpochUtil.getDayWithYearMonth(dateYM);
			for (int i = 0; i < days; i++) {
				startTime = YearMonthToEpochUtil.addDayEpochSecond(dateYM, i);
				Long endTime = YearMonthToEpochUtil.addDayEpochSecond(dateYM, i + 1);
				Mono<String[]> matchHistoryByPuuid = apiService.getMatchHistoryByPuuid(puuid, startTime, endTime, start,
					100); //100개의 매치를 가져옴
				String[] matchHistory = matchHistoryByPuuid.block();
				// Set<MatchDateStreak> matchDateStreaks = user.getMatchDateStreaks();
				// for (String matchId : matchHistory) {
				// 	MatchParticipant matchParticipant = new MatchParticipant(matchId, user);
				// 	matchParticipant.setMatchId(matchId);
				// 	matchParticipant.setDate(dateYM);
				// 	matchParticipants.add(matchParticipant);
				// }
				// MatchParticipant matchParticipant = new MatchParticipant();
				// user.addMatchParticipant();
			}

			userRepository.save(user);
		} catch (Exception e) {
			e.printStackTrace();
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
			String puuid = setUserProfile(name, tag);
			if (puuid == null) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "summoner not found");
			}
			System.out.println("ERROR :::: puuid = " + puuid);
			userOptional = userRepository.findById(puuid);
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
