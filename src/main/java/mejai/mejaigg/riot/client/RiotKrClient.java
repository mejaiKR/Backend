package mejai.mejaigg.riot.client;

import java.util.Set;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import mejai.mejaigg.riot.dto.RankDto;
import mejai.mejaigg.riot.dto.SummonerDto;

@FeignClient(
	name = "RiotKrClient",
	url = "https://kr.api.riotgames.com"
)
public interface RiotKrClient {

	@GetMapping("/lol/summoner/v4/summoners/by-puuid/{puuid}")
	SummonerDto getSummonerByPuuuid(@RequestParam("api_key") String riotKey, @PathVariable String puuid);

	@GetMapping("/lol/league/v4/entries/by-summoner/{summonerId}")
	Set<RankDto> getRankBySummonerId(@RequestParam("api_key") String riotKey, @PathVariable String summonerId);

	@Deprecated
	@GetMapping("/lol/summoner/v4/summoners/by-name/{name}")
	SummonerDto getSummerByName(@RequestParam("api_key") String riotKey, @PathVariable String name);

}
