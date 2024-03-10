package mejai.mejaigg.service;
import mejai.mejaigg.dto.riot.SummonerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.http.HttpClient;

@Service
@PropertySource(ignoreResourceNotFound = false, value = "classpath:riotApiKey.properties")
public class ApiService {
	private final WebClient webClient;
	static final String SERVERURL = "https://kr.api.riotgames.com";
	@Value("${riot.api.key}")
	private String riotKey;

	@Autowired
	public ApiService(WebClient.Builder webClientBuilder){
		this.webClient = webClientBuilder.baseUrl(SERVERURL).build();
	}

	public Mono<SummonerDTO> getSummonerByName(String summonerName){
		return this.webClient.get()
			.uri(uriBuilder -> uriBuilder
				.path("/lol/summoner/v4/summoners/by-name/{name}")
				.queryParam("api_key", riotKey)
				.build())
			.retrieve()
			.bodyToMono(SummonerDTO.class);
	}

	public Mono<SummonerDTO> getSummonerByNameAndTag(String summonerName, String tag){
		return this.webClient.get()
			.uri(uriBuilder -> uriBuilder
				.path("/riot/account/v1/accounts/by-riot-id/{summonerName}/{tag}")
				.queryParam("api_key", riotKey)
				.build())
			.retrieve()
			.bodyToMono(SummonerDTO.class);
	}

}
