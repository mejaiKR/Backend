package mejai.mejaigg.summoner.service;

import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mejai.mejaigg.global.config.RiotProperties;
import mejai.mejaigg.rank.dto.RankDto;
import mejai.mejaigg.rank.repository.RankRepository;
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
	private final RankRepository rankRepository;
	private final RiotProperties riotProperties;

	@Transactional
	public UserProfileDto getUserProfileByNameTag(String name, String tag) {
		//유저를 DB에서 먼저 찾아봅니다.
		Summoner summoner = summonerRepository.findBySummonerNameAndTagLineAllIgnoreCase(name, tag).orElse(null);
		UserProfileDto userProfileDto = new UserProfileDto();
		if (summoner == null) {
			//유저가 없으면 riot api를 통해 유저 정보를 가져옵니다.
			AccountDto accountDto = riotService.getAccountByNameAndTag(name, tag);
			SummonerDto summonerDto = riotService.getSummonerByPuuid(accountDto.getPuuid());
			Set<RankDto> rankDtos = riotService.getRankBySummonerId(summonerDto.getId());

			summoner = Summoner.builder()
				.summonerName(accountDto.getGameName())
				.tagLine(accountDto.getTagLine())
				.puuid(accountDto.getPuuid())
				.accountId(summonerDto.getAccountId())
				.summonerId(summonerDto.getId())
				.summonerLevel(summonerDto.getSummonerLevel())
				.revisionDate(summonerDto.getRevisionDate())
				.profileIconId(summonerDto.getProfileIconId())
				.summonerLevel(summonerDto.getSummonerLevel())
				.build();
			summonerRepository.save(summoner);
			summoner.setRankByRankDtos(rankDtos);
			rankRepository.saveAll(summoner.getRanks());
			summonerRepository.save(summoner);
		}
		// 유저가 있으면 바로 유저 정보를 리턴
		userProfileDto.setBySummoner(summoner, riotProperties.getResourceUrl());
		return userProfileDto;
	}

	@Transactional(readOnly = false)
	public String updateUserProfile(String name, String tag) { //처음 콜 할 때 세팅 되는 함수
		AccountDto accountDto = riotService.getAccountByNameAndTag(name, tag);
		SummonerDto summonerDto = riotService.getSummonerByPuuid(accountDto.getPuuid());
		Set<RankDto> rankDtos = riotService.getRankBySummonerId(summonerDto.getId());

		// Summoner user = UserMapper.INSTANCE.toUserEntity(accountDto, summonerDto);
		// user.setRank(rankService.createRanks(rankDtos, user));
		// summonerRepository.save(user);
		return null;
	}

	private void updateUserDetails(Summoner user) {
		// SummonerDto summoner = riotService.getSummonerByPuuid(user.getId());
		// user.updateBySummonerDto(summoner);
		// summonerRepository.save(user); // 사용자를 저장하여 변경 사항을 반영
	}
}

