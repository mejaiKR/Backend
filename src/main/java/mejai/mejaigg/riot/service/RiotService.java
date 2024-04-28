package mejai.mejaigg.riot.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
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
@PropertySource(ignoreResourceNotFound = false, value = "classpath:riotApiKey.properties")
public class RiotService {
	private final RiotKrClient riotKrClient;
	private final RiotAsiaClient riotAsiaClient;
	@Value("${riot.api.key}")
	private String riotKey;

	public SummonerDto getSummonerByName(String name) {
		return riotKrClient.getSummerByName(riotKey, name);
	}

	public SummonerDto getSummonerByPuuid(String puuid) {
		return riotKrClient.getSummonerByPuuuid(riotKey, puuid);
	}

	public AccountDto getAccountByNameAndTag(String summonerName, String tag) {
		return riotAsiaClient.getAccountByNameAndTag(riotKey, summonerName, tag);
	}

	public Set<RankDto> getRankBySummonerId(String summonerId) {
		return riotKrClient.getRankBySummonerId(riotKey, summonerId);
	}

	public String[] getMatchHistoryByPuuid(String puuid, Long startTime, Long endTime, Long start, int count) {
		return riotAsiaClient.getMatchHistoryByPuuid(riotKey, puuid, startTime, endTime, start, count);
	}

	public MatchDto getMatchDtoByMatchId(String matchId) {
		return riotAsiaClient.getMatchDtoByMatchId(riotKey, matchId);
	}

}
