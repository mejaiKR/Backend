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
import mejai.mejaigg.riot.dto.AccountDto;
import mejai.mejaigg.riot.dto.SummonerDto;
import mejai.mejaigg.riot.service.RiotService;
import mejai.mejaigg.summoner.domain.Summoner;
import mejai.mejaigg.summoner.dto.response.RenewalStatusResponse;
import mejai.mejaigg.summoner.dto.response.SummonerProfileResponse;
import mejai.mejaigg.summoner.repository.SummonerRepository;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SummonerService {

	private final RiotService riotService;
	private final SummonerRepository summonerRepository;
	private final RiotProperties riotProperties;

	/**
	 * 소환사 이름과 태그를 통해 소환사 정보를 가져옵니다.
	 * 만약 소환사가 없으면 riot api를 통해 소환사 정보를 가져옵니다.
	 * 소환사가 있으면 바로 소환사 정보를 리턴합니다.(라이엇 API를 호출하지 않습니다.)
	 *
	 * @param name 소환사 이름
	 * @param tag  소환사 태그
	 * @return 소환사 정보
	 */
	@Transactional
	public SummonerProfileResponse getSummonerProfileByNameTag(String name, String tag) {
		Summoner summoner = findOrCreateSummoner(name, tag);

		return new SummonerProfileResponse(summoner, riotProperties.getResourceUrl());
	}

	@Transactional
	public Summoner findOrCreateSummoner(String name, String tag) {
		String normalizeName = name.replace(" ", "").toLowerCase();
		String normalizeTag = tag.toLowerCase();

		return summonerRepository
			.findByNormalizedSummonerNameAndNormalizedTagLine(normalizeName, normalizeTag)
			.orElseGet(() -> initializeSummonerData(name, tag));
	}

	/**
	 * 소환사 정보를 초기화합니다.
	 * 처음 검색 때 사용하는 함수입니다.
	 * 3번의 라이엇 API 호출을 통해 소환사 정보를 가져옵니다.
	 *
	 * @param name 소환사 이름
	 * @param tag  소환사 태그
	 * @return 초기화된 소환사 정보
	 */
	private Summoner initializeSummonerData(String name, String tag) {
		log.info("소환사 정보가 없어 새로 생성합니다.");
		AccountDto accountDto = riotService.getAccountByNameAndTag(name, tag);
		SummonerDto summonerDto = riotService.getSummonerByPuuid(accountDto.getPuuid());
		Set<RankDto> rankDtos = riotService.getRankBySummonerId(summonerDto.getId());

		Summoner summoner = Summoner.builder()
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
		summoner.setRankByRankDtos(rankDtos);
		// rankRepository.saveAll(summoner.getRanks());
		return summonerRepository.save(summoner);
	}

	/**
	 * 소환사 이름과 태그를 통해 소환사 정보를 갱신합니다.
	 * 갱신 주기는 2시간입니다.
	 * 2번의 라이엇 API 호출을 통해 소환사 정보를 가져옵니다.
	 *
	 * @param name 소환사 이름
	 * @param tag  소환사 태그
	 * @return 갱신된 소환사 정보
	 */
	@Transactional
	public SummonerProfileResponse renewalSummonerProfileByNameTag(String name, String tag) {
		Summoner summoner = findOrCreateSummoner(name, tag);

		if (summoner.getUpdatedAt().plusHours(2).isAfter(LocalDateTime.now())) {
			log.info("2시간이 지나지 않아 강제 업데이트를 할 수 없습니다.");
		} else {
			log.info("2시간이 지나 강제 업데이트를 진행합니다.");
			updateUserDetails(summoner);
		}

		return new SummonerProfileResponse(summoner, riotProperties.getResourceUrl());
	}

	public RenewalStatusResponse getProfileRenewalStatus(String summonerName, String tag) {
		Summoner summoner = findOrCreateSummoner(summonerName, tag);

		return new RenewalStatusResponse(summoner.getUpdatedAt());
	}

	/**
	 * 소환사 정보를 갱신합니다.
	 * 이건 이미 유저가 한번 검색 됐다고 생각하고 랭크와 레벨을 업데이트 하는 것 입니다.
	 *
	 * @param summoner 소환사 정보
	 */
	private void updateUserDetails(Summoner summoner) {
		SummonerDto summonerDto = riotService.getSummonerByPuuid(summoner.getPuuid());
		Set<RankDto> rankDtos = riotService.getRankBySummonerId(summonerDto.getId());
		List<Rank> ranks = summoner.getRanks();
		summoner.setRankByRankDtos(rankDtos);
		ranks.forEach(rank ->
			rank.updateByRankDto(rankDtos.stream()
				.filter(rankDto -> rankDto.getQueueType().equals(rank.getId().getQueueType()))
				.findFirst()
				.orElse(null)
			)
		);
		summoner.updateBySummonerDto(summonerDto);
		summoner.setRanks(ranks);
		summoner.setUpdatedAt(LocalDateTime.now());
		summonerRepository.save(summoner);
	}
}

