package kr.mejai.project.external.riot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import kr.mejai.project.common.config.RiotProperties;
import kr.mejai.project.domain.rank.dto.RankDto;
import kr.mejai.project.external.riot.client.RiotAsiaClient;
import kr.mejai.project.external.riot.client.RiotKrClient;
import kr.mejai.project.external.riot.dto.AccountDto;
import kr.mejai.project.external.riot.dto.SummonerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(RiotProperties.class)
public class RiotService {
	private final RiotKrClient riotKrClient;
	private final RiotAsiaClient riotAsiaClient;
	private final RiotProperties riotProperties;

	public SummonerDto getSummonerByPuuid(String puuid) {
		return riotKrClient.getSummonerByPuuuid(riotProperties.getApiKey(), puuid);
	}

	public AccountDto getAccountByNameAndTag(String summonerName, String tag) {
		return riotAsiaClient.getAccountByNameAndTag(riotProperties.getApiKey(), summonerName, tag);
	}

	public Set<RankDto> getRankBySummonerId(String summonerId) {
		return riotKrClient.getRankBySummonerId(riotProperties.getApiKey(), summonerId);
	}

	public Set<RankDto> getRankByPuuid(String puuid) {
		return riotKrClient.getRankByPuuid(riotProperties.getApiKey(), puuid);
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
