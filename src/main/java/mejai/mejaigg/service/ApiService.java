package mejai.mejaigg.service;
import mejai.mejaigg.dto.riot.AccountDto;
import mejai.mejaigg.dto.riot.RankDto;
import mejai.mejaigg.dto.riot.SummonerDto;
import mejai.mejaigg.dto.riot.match.MatchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Set;

//TODO : 모든 클래스 다른 statusCode 에 대한 예외처리 추가하기
@Service
@PropertySource(ignoreResourceNotFound = false, value = "classpath:riotApiKey.properties")
public class ApiService {
	private final WebClient webClient;
	private final WebClient webClient2;
	static final String SERVERURL = "https://kr.api.riotgames.com";
	static final String SERVERURL2 = "https://asia.api.riotgames.com";
	@Value("${riot.api.key}")
	private String riotKey;

	@Autowired
	public ApiService(WebClient.Builder webClientBuilder){
		this.webClient = webClientBuilder.baseUrl(SERVERURL).build();
		this.webClient2 = webClientBuilder.baseUrl(SERVERURL2).build();
	}

	public Mono<SummonerDto> getSummonerByName(String name){
		return this.webClient.get()
			.uri(uriBuilder -> uriBuilder
				.path("/lol/summoner/v4/summoners/by-name/{name}")
				.queryParam("api_key", riotKey)
				.build(name))
			.retrieve()
			.onStatus(clientResponse ->clientResponse.is4xxClientError(),
				clientResponse->Mono.error(new RuntimeException("4xx error")))
			.onStatus(clientResponse ->clientResponse.is5xxServerError(),
				clientResponse->Mono.error(new RuntimeException("5xx error")))
			.bodyToMono(SummonerDto.class);
	}

	public Mono<SummonerDto> getSummonerByPuuid(String puuid){
		return this.webClient.get()
			.uri(uriBuilder -> uriBuilder
				.path("/lol/summoner/v4/summoners/by-puuid/{puuid}")
				.queryParam("api_key", riotKey)
				.build(puuid))
			.retrieve()
			.onStatus(clientResponse ->clientResponse.is4xxClientError(),
				clientResponse->Mono.error(new RuntimeException("4xx error")))
			.onStatus(clientResponse ->clientResponse.is5xxServerError(),
				clientResponse->Mono.error(new RuntimeException("5xx error")))
			.bodyToMono(SummonerDto.class);
	}

	public Mono<AccountDto> getAccountByNameAndTag(String summonerName, String tag){
		return this.webClient2.get()
			.uri(uriBuilder -> uriBuilder
				.path("/riot/account/v1/accounts/by-riot-id/{summonerName}/{tag}")
				.queryParam("api_key", riotKey)
				.build(summonerName,tag))
			.retrieve()
			.onStatus(clientResponse ->clientResponse.is4xxClientError(),
				clientResponse->Mono.error(new RuntimeException("4xx error")))
			.onStatus(clientResponse ->clientResponse.is5xxServerError(),
				clientResponse->Mono.error(new RuntimeException("5xx error")))
			.bodyToMono(AccountDto.class);
	}

	public Mono<Set<RankDto>> getRankBySummonerId(String summonerId){
		return this.webClient.get()
			.uri(uriBuilder -> uriBuilder
				.path("/lol/league/v4/entries/by-summoner/{summonerId}")
				.queryParam("api_key", riotKey)
				.build(summonerId))
			.retrieve()
			.onStatus(clientResponse ->clientResponse.is4xxClientError(),
				clientResponse->Mono.error(new RuntimeException("4xx error")))
			.onStatus(clientResponse ->clientResponse.is5xxServerError(),
				clientResponse->Mono.error(new RuntimeException("5xx error")))
			.bodyToMono(new ParameterizedTypeReference<Set<RankDto>>() {
			});
	}

	public Mono<String[]> getMatchHistoryByPuuid(String puuid, int count){ //count는 최대 100까지
		return this.webClient2.get()
			.uri(uriBuilder -> uriBuilder
				.path("/lol/match/v5/matches/by-puuid/{puuid}/ids")
				.queryParam("start",0)
				.queryParam("count",count)
				.queryParam("api_key", riotKey)
				.build(puuid,count))
			.retrieve()
			.onStatus(clientResponse ->clientResponse.is4xxClientError(),
				clientResponse->Mono.error(new RuntimeException("4xx error")))
			.onStatus(clientResponse ->clientResponse.is5xxServerError(),
				clientResponse->Mono.error(new RuntimeException("5xx error")))
			.bodyToMono(String[].class);
	}

	public Mono<MatchDto> getMatchDtoByMatchId(String matchId){
		return this.webClient2.get()
			.uri(uriBuilder -> uriBuilder
				.path("/lol/match/v5/matches/{matchId}")
				.queryParam("api_key", riotKey)
				.build(matchId))
			.retrieve()
			.onStatus(clientResponse ->clientResponse.is4xxClientError(),
				clientResponse->Mono.error(new RuntimeException("4xx error")))
			.onStatus(clientResponse ->clientResponse.is5xxServerError(),
				clientResponse->Mono.error(new RuntimeException("5xx error")))
			.bodyToMono(MatchDto.class);
	}
}
