package mejai.mejaigg.summoner.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mejai.mejaigg.rank.RankService;
import mejai.mejaigg.rank.dto.RankDto;
import mejai.mejaigg.riot.dto.AccountDto;
import mejai.mejaigg.riot.dto.SummonerDto;
import mejai.mejaigg.riot.service.RiotService;
import mejai.mejaigg.summoner.domain.Summoner;
import mejai.mejaigg.summoner.dto.response.UserProfileDto;
import mejai.mejaigg.summoner.repository.SummonerRepository;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProfileService {

	private final RiotService riotService;
	private final SummonerRepository summonerRepository;
	private final RankService rankService;

	@Value("${variables.resourceURL:https://ddragon.leagueoflegends.com/cdn/11.16.1/img/profileicon/}")
	private String resourceURL;

	@Transactional(readOnly = false)
	public String setUserProfile(String name, String tag) { //처음 콜 할 때 세팅 되는 함수
		AccountDto accountDto = riotService.getAccountByNameAndTag(name, tag);
		SummonerDto summonerDto = riotService.getSummonerByPuuid(accountDto.getPuuid());
		Set<RankDto> rankDtos = riotService.getRankBySummonerId(summonerDto.getId());

		// Summoner user = UserMapper.INSTANCE.toUserEntity(accountDto, summonerDto);
		//
		// user.setRank(rankService.createRanks(rankDtos, user));
		// summonerRepository.save(user);
		return null;
	}

	@Transactional
	public UserProfileDto getUserProfileByNameTag(String name, String tag) {
		Optional<Summoner> userOptional = summonerRepository.findBySummonerNameAndTagLineAllIgnoreCase(name, tag);
		if (userOptional.isEmpty()) {
			String puuid = setUserProfile(name, tag);
			if (puuid == null) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "summoner not found");
			}
			userOptional = summonerRepository.findById(puuid);
		} else { // 2시간이 지나면 업데이트
			Summoner user = userOptional.get();
			LocalDateTime lastUpdatedAt = user.getUpdatedAt();
			LocalDateTime now = LocalDateTime.now();
			if (Duration.between(lastUpdatedAt, now).toHours() >= 2) {
				updateUserDetails(user); //유저 업데이트
				rankService.updateUserRanks(user); // 랭크 업데이트
			}
		}
		UserProfileDto userProfileDto = new UserProfileDto();
		if (userOptional.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "summoner not found");
		}
		Summoner user = userOptional.get();
		userProfileDto.setByUser(user, resourceURL);
		return userProfileDto;
	}

	private void updateUserDetails(Summoner user) {
		// SummonerDto summoner = riotService.getSummonerByPuuid(user.getId());
		// user.updateBySummonerDto(summoner);
		// summonerRepository.save(user); // 사용자를 저장하여 변경 사항을 반영
	}
}

