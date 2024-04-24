package mejai.mejaigg.riot.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import mejai.mejaigg.riot.dto.AccountDto;
import mejai.mejaigg.riot.dto.match.MatchDto;

@FeignClient(
	name = "RiotAsiaClient",
	url = "https://asia.api.riotgames.com"
)
public interface RiotAsiaClient {
	@GetMapping("/riot/account/v1/accounts/by-riot-id/{summonerName}/{tag}")
	AccountDto getAccountByNameAndTag(@RequestParam("api_key") String riotKey, @PathVariable String summonerName,
		@PathVariable String tag);

	@GetMapping("/lol/match/v5/matches/by-puuid/{puuid}/ids")
	String[] getMatchHistoryByPuuid(@RequestParam("api_key") String riotKey, @PathVariable String puuid,
		@RequestParam("startTime") Long startTime,
		@RequestParam("endTime") Long endTime, @RequestParam("start") Long start,
		@RequestParam("count") int count);

	@GetMapping("/lol/match/v5/matches/{matchId}")
	MatchDto getMatchDtoByMatchId(@RequestParam("api_key") String riotKey, @PathVariable String matchId);
}
