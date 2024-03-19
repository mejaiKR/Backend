package mejai.mejaigg.service;
import mejai.mejaigg.domain.*;
import mejai.mejaigg.dto.riot.AccountDto;
import mejai.mejaigg.dto.riot.RankDto;
import mejai.mejaigg.dto.riot.SummonerDto;
import mejai.mejaigg.dto.riot.match.MatchDto;
import mejai.mejaigg.dto.riot.match.ParticipantDto;
import mejai.mejaigg.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import mejai.mejaigg.dto.response.UserProfileDto;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

	private final ApiService apiService;
	private final UserRepository userRepository;
	private final GameRepository gameRepository;
	private final UserGameStatRepository userGameStatRepository;

	@Value("${season:14}") // 기본값으로 14을 사용
	private int season;

	//TODO : api 콜을 실패하는 경우 고려해야함
	//TODO : 비동기 레포지토리 방식 적용
	//TODO : 시즌 바꼈을때 추가하는 로직 필요하다.
	//처음으로 요청이 들어왔을 때 호출되는 서비스
	@Transactional(readOnly = false)
	public String setUserProfile(String name,String tag){
		User user = new User();
		Mono<AccountDto> account = apiService.getAccountByNameAndTag(name, tag);
		try{
			AccountDto accountDto = account.block();
			user.setByAccountDto(accountDto);
			Mono<SummonerDto> summoner = apiService.getSummonerByPuuid(accountDto.getPuuid());
			SummonerDto summonerDto = summoner.block();
			user.setBySummonerDto(summonerDto);
			Mono<Set<RankDto>> rankBySummonerId = apiService.getRankBySummonerId(summonerDto.getId());
			Set<RankDto> rankDtos = rankBySummonerId.block();
			RankDto firstElement = rankDtos.stream().findFirst().orElse(null);
			Rank rank = new Rank();
			rank.setRankByRankDto(firstElement,season);
			user.addRank(rank);
			userRepository.save(user);
			Mono<String[]> matchHistoryByPuuid = apiService.getMatchHistoryByPuuid(accountDto.getPuuid(), 20);
			String[] matchHistory = matchHistoryByPuuid.block();
			for (String matchId : matchHistory) {
				Game game = new Game();
				MatchParticipant matchParticipant = new MatchParticipant();
				Mono<MatchDto> gameInfo = apiService.getMatchDtoByMatchId(matchId);
				MatchDto matchDto = gameInfo.block();

				game.setByMatchDto(matchDto);
				user.addMatchParticipant(matchParticipant);
				game.addMatchParticipant(matchParticipant);
				ParticipantDto[] participants = matchDto.getInfo().getParticipants();
				for(ParticipantDto participant : participants){
					UserGameStat userGameStat = new UserGameStat();
					userGameStat.setByParticipantDto(participant);
					game.addGameStat(userGameStat);
					userGameStatRepository.save(userGameStat);
				}
				gameRepository.save(game);
			}
			userRepository.save(user);
		}catch (Exception e){
			System.out.println("에러가 발생!!");
			return null;
		}
		//Todo: 가장 마지막 전적의 날짜를 저장해야만 함. (그 이후부터 조회하기 위해서)
		//Todo: 만약 100개를 받아오면 해당 마지막을 확인해서 다시 요청 후 재조회 해야만 한다.
		//Todo: 한번 호출 한적 있다면 가장 마지막 날짜를 기준으로 다시 요청

		return user.getPuuid();
	}




	@Transactional
	public UserProfileDto getUserProfileByName(String name) {
		//이전에 유저가 가입한 적 있는 경우
		return null;
	}

	@Transactional
	public UserProfileDto getUserProfileByNameTag(String name,String tag) {
		User user = userRepository.findOneWithNameAndTag(name, tag);
		if (user == null){
			String puuid = setUserProfile(name,tag);
			System.out.println("puuid = " + puuid);;
			if (puuid == null){;
				throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "summoner not found"
				);
			}
			user = userRepository.findOneWithRank(puuid);
		}
		UserProfileDto userProfileDto = new UserProfileDto();
		userProfileDto.setByUser(user);
		return userProfileDto;
	}
}
