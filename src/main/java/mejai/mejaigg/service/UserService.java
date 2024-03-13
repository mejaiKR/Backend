package mejai.mejaigg.service;
import mejai.mejaigg.domain.Rank;
import mejai.mejaigg.domain.User;
import mejai.mejaigg.dto.riot.AccountDto;
import mejai.mejaigg.dto.riot.RankDto;
import mejai.mejaigg.dto.riot.SummonerDto;
import mejai.mejaigg.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import mejai.mejaigg.dto.response.UserProfileDto;
import reactor.core.publisher.Mono;

import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

	private final ApiService apiService;
	private final UserRepository userRepository;
	@Value("${season:14}") // 기본값으로 14을 사용
	private int season;

	//TODO : api 콜을 실패하는 경우 고려해야함
	//TODO : 비동기 레포지토리 방식 적용
	//TODO : 시즌 바꼈을때 추가하는 로직 필요하다.
	//처음으로 요청이 들어왔을 때 호출되는 서비스
	@Transactional(readOnly = false)
	public void setUserProfile(String name,String tag){
		User user = new User();
		Mono<AccountDto> account = apiService.getAccountByNameAndTag(name, tag);
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
	}




	@Transactional
	public UserProfileDto getUserProfileByName(String name) {
		//이전에 유저가 가입한 적 있는 경우
//		if (userRepository.existsByName(name)) {
//			User user = userRepository.findByName(name);
//
//		}
		return null;
	}

//	@Transactional
//	public UserProfileDto getUserProfileByNameTag(String name,String tag) {
//		//이전에 유저가 가입한 적 있는 경우
//	}
}
