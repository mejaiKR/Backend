package mejai.mejaigg.summoner.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mejai.mejaigg.global.config.RiotProperties;
import mejai.mejaigg.riot.service.RiotService;
import mejai.mejaigg.summoner.domain.Summoner;
import mejai.mejaigg.summoner.dto.response.RenewalStatusResponse;
import mejai.mejaigg.summoner.dto.response.SummonerProfileResponse;
import mejai.mejaigg.summoner.dto.response.SummonerSearchResponse;
import mejai.mejaigg.summoner.repository.SummonerRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class SummonerService {

	private final RiotService riotService;
	private final SummonerTransactionService summonerTransactionService;
	private final SummonerRepository summonerRepository;
	private final RiotProperties riotProperties;

	public Summoner findOrCreateSummoner(String name, String tag) {
		return summonerTransactionService.findOrCreateSummoner(name, tag);
	}

	/**
	 * 소환사 이름과 태그를 통해 소환사 정보를 가져옵니다.
	 * 만약 소환사가 없으면 riot api를 통해 소환사 정보를 가져옵니다.
	 * 소환사가 있으면 바로 소환사 정보를 리턴합니다.(라이엇 API를 호출하지 않습니다.)
	 *
	 * @param name 소환사 이름
	 * @param tag  소환사 태그
	 * @return 소환사 정보
	 */
	public SummonerProfileResponse getSummonerProfileByNameTag(String name, String tag) {
		Summoner summoner = findOrCreateSummoner(name, tag);

		return new SummonerProfileResponse(summoner, riotProperties.getResourceUrl());
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
	public SummonerProfileResponse renewalSummonerProfileByNameTag(String name, String tag) {
		Summoner summoner = findOrCreateSummoner(name, tag);

		if (summoner.getUpdatedAt().plusHours(2).isAfter(LocalDateTime.now())) {
			log.info("2시간이 지나지 않아 강제 업데이트를 할 수 없습니다.");
		} else {
			log.info("2시간이 지나 강제 업데이트를 진행합니다.");
			summonerTransactionService.updateUserDetails(summoner);
		}

		return new SummonerProfileResponse(summoner, riotProperties.getResourceUrl());
	}

	public RenewalStatusResponse getProfileRenewalStatus(String summonerName, String tag) {
		Summoner summoner = findOrCreateSummoner(summonerName, tag);

		return new RenewalStatusResponse(summoner.getUpdatedAt());
	}

	public SummonerSearchResponse searchSummoner(String summonerName, int count) {
		List<Summoner> summoners = summonerRepository.findBySummonerNameContainingAllIgnoreCaseOrderBySummonerNameDesc(
			summonerName,
			Limit.of(count)
		);

		return new SummonerSearchResponse(summoners);
	}

}
