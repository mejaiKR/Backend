package mejai.mejaigg.watch.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mejai.mejaigg.match.service.MatchService;
import mejai.mejaigg.summoner.domain.Summoner;
import mejai.mejaigg.summoner.repository.SummonerRepository;
import mejai.mejaigg.summoner.service.ProfileService;
import mejai.mejaigg.watch.dto.response.WatchSummonerDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class WatchService {
	private final SummonerRepository summonerRepository;
	private final ProfileService profileService;
	private final MatchService matchService;

	@Transactional
	public WatchSummonerDto watchSummoner(String summonerName, String tag) {
		Summoner summoner = summonerRepository.findBySummonerNameAndTagLineAllIgnoreCase(summonerName, tag).orElse(null);
		if (summoner == null) {
			summoner = profileService.initializeSummonerData(summonerName, tag);
		}

		matchService.createMatches(summoner.getPuuid());

		return new WatchSummonerDto(summoner.getId());
	}
}
