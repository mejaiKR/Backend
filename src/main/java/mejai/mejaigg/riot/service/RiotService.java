package mejai.mejaigg.riot.service;

import java.util.Set;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mejai.mejaigg.rank.dto.RankDto;
import mejai.mejaigg.riot.client.RiotAsiaClient;
import mejai.mejaigg.riot.client.RiotKrClient;
import mejai.mejaigg.riot.dto.AccountDto;
import mejai.mejaigg.riot.dto.SummonerDto;
import mejai.mejaigg.riot.dto.match.MatchDto;

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

	public MatchDto getMatchDtoByMatchId(String matchId) {
		return riotAsiaClient.getMatchDtoByMatchId(riotProperties.getApiKey(), matchId);
	}

}
