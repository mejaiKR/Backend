package mejai.mejaigg.riot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mejai.mejaigg.global.config.RiotProperties;
import mejai.mejaigg.rank.dto.RankDto;
import mejai.mejaigg.riot.client.RiotAsiaClient;
import mejai.mejaigg.riot.client.RiotKrClient;
import mejai.mejaigg.riot.dto.AccountDto;
import mejai.mejaigg.riot.dto.SummonerDto;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(RiotProperties.class)
public class RiotService {
	private final RiotKrClient riotKrClient;
	private final RiotAsiaClient riotAsiaClient;
	private final RiotProperties riotProperties;

	public SummonerDto getSummonerByName(String name) {
		return riotKrClient.getSummerByName(riotProperties.getApiKey(), name);
	}

	public SummonerDto getSummonerByPuuid(String puuid) {
		return riotKrClient.getSummonerByPuuuid(riotProperties.getApiKey(), puuid);
	}

	public AccountDto getAccountByNameAndTag(String summonerName, String tag) {
		return riotAsiaClient.getAccountByNameAndTag(riotProperties.getApiKey(), summonerName, tag);
	}

	public Set<RankDto> getRankBySummonerId(String summonerId) {
		return riotKrClient.getRankBySummonerId(riotProperties.getApiKey(), summonerId);
	}

	public String[] getMatchHistoryByPuuid(String puuid, Long startTime, Long endTime, Long start, int count) {
		return riotAsiaClient.getMatchHistoryByPuuid(riotProperties.getApiKey(), puuid, startTime, endTime, start,
			count);
	}

	public List<String> getMatchIds(String puuid) {
		return List.of(getMatchHistoryByPuuid(puuid, null, null, null, 100));
	}

	public List<String> getMatches(List<String> matchIds) {
		List<String> matches = new ArrayList<>();
		for (String matchId : matchIds) {
			String matchResponse = riotAsiaClient.getMatchByMatchId(riotProperties.getApiKey(), matchId);
			matches.add(matchResponse);
		}

		return matches;
	}
}
