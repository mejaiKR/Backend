package mejai.mejaigg.summoner.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mejai.mejaigg.global.config.RiotProperties;
import mejai.mejaigg.rank.domain.Rank;
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

	/**
	 * 소환사 이름과 태그를 통해 소환사 정보를 가져옵니다.
	 * 만약 소환사가 없으면 riot api를 통해 소환사 정보를 가져옵니다.
	 * 소환사가 있으면 바로 소환사 정보를 리턴합니다.(라이엇 API를 호출하지 않습니다.)
	 * @param name 소환사 이름
	 * @param tag 소환사 태그
	 * @return 소환사 정보
	 */
	@Transactional
	public UserProfileDto getUserProfileByNameTag(String name, String tag) {
		//유저를 DB에서 먼저 찾아봅니다.
		Summoner summoner = summonerRepository.findBySummonerNameAndTagLineAllIgnoreCase(name, tag).orElse(null);
		UserProfileDto userProfileDto = new UserProfileDto();
		if (summoner == null)//유저가 없으면 riot api를 통해 유저 정보를 가져옵니다.
			summoner = initializeSummonerData(name, tag);
		// 유저가 있으면 바로 유저 정보를 리턴
		userProfileDto.setBySummoner(summoner, riotProperties.getResourceUrl());
		return userProfileDto;
	}

	/**
	 * 소환사 정보를 초기화합니다.
	 * 처음 검색 때 사용하는 함수입니다.
	 * 3번의 라이엇 API 호출을 통해 소환사 정보를 가져옵니다.
	 * @param name 소환사 이름
	 * @param tag 소환사 태그
	 * @return 초기화된 소환사 정보
	 */
	private Summoner initializeSummonerData(String name, String tag) {
		Summoner summoner;
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
		// rankRepository.saveAll(summoner.getRanks());
		summonerRepository.save(summoner);
		return summoner;
	}

	/**
	 * 소환사 이름과 태그를 통해 소환사 정보를 갱신합니다.
	 * 갱신 주기는 2시간입니다.
	 * 2번의 라이엇 API 호출을 통해 소환사 정보를 가져옵니다.
	 * @param name 소환사 이름
	 * @param tag 소환사 태그
	 * @return 갱신된 소환사 정보
	 */
	@Transactional
	public UserProfileDto refreshUserProfileByNameTag(String name, String tag) {
		Summoner summoner = summonerRepository.findBySummonerNameAndTagLineAllIgnoreCase(name, tag).orElse(null);
		if (summoner == null) {
			summoner = initializeSummonerData(name, tag);
		} else {
			if (summoner.getUpdatedAt().plusHours(2).isAfter(LocalDateTime.now()))
				log.info("2시간이 지나지 않아 강제 업데이트를 할 수 없습니다.");
			else
				updateUserDetails(summoner);
		}
		UserProfileDto userProfileDto = new UserProfileDto();
		userProfileDto.setBySummoner(summoner, riotProperties.getResourceUrl());
		return userProfileDto;
	}

	/**
	 * 소환사 정보를 갱신합니다.
	 * 이건 이미 유저가 한번 검색 됐다고 생각하고 랭크와 레벨을 업데이트 하는 것 입니다.
	 * @param summoner 소환사 정보
	 */
	private void updateUserDetails(Summoner summoner) {
		SummonerDto summonerDto = riotService.getSummonerByPuuid(summoner.getPuuid());
		Set<RankDto> rankDtos = riotService.getRankBySummonerId(summonerDto.getId());
		List<Rank> ranks = summoner.getRanks();
		ranks.forEach(rank ->
			rank.updateByRankDto(rankDtos.stream()
				.filter(rankDto -> rankDto.getQueueType().equals(rank.getId().getQueueType()))
				.findFirst()
				.orElseThrow(
					() -> new IllegalArgumentException("Rank 정보가 없습니다.") // Rank 정보가 없으면 예외를 던집니다.
				)
			)
		);
		summoner.updateBySummonerDto(summonerDto);
		summoner.setRanks(ranks);
		summonerRepository.save(summoner); // 사용자를 저장하여 변경 사항을 반영
	}
}

