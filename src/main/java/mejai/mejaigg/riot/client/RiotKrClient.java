package mejai.mejaigg.riot.client;

import java.util.Set;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ServerErrorException;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import mejai.mejaigg.riot.exception.ClientErrorCode;
import mejai.mejaigg.riot.exception.ClientException;
import mejai.mejaigg.rank.dto.RankDto;
import mejai.mejaigg.riot.dto.SummonerDto;

@FeignClient(
	name = "RiotKrClient",
	url = "https://kr.api.riotgames.com",
	fallbackFactory = RiotKrClient.RiotKrClientFallback.class
)
public interface RiotKrClient {

	@Retryable(
		value = {ServerErrorException.class}, // 재시도할 예외 지정
		maxAttempts = 3, // 최대 재시도 횟수
		backoff = @Backoff(delay = 2000) // 재시도 간격
	)
	@GetMapping("/lol/summoner/v4/summoners/by-puuid/{puuid}")
	SummonerDto getSummonerByPuuuid(@RequestParam("api_key") String riotKey, @PathVariable String puuid);

	@Retryable(
		value = {ServerErrorException.class}, // 재시도할 예외 지정
		maxAttempts = 3, // 최대 재시도 횟수
		backoff = @Backoff(delay = 2000) // 재시도 간격
	)
	@GetMapping("/lol/league/v4/entries/by-summoner/{summonerId}")
	Set<RankDto> getRankBySummonerId(@RequestParam("api_key") String riotKey, @PathVariable String summonerId);

	@Deprecated
	@GetMapping("/lol/summoner/v4/summoners/by-name/{name}")
	SummonerDto getSummerByName(@RequestParam("api_key") String riotKey, @PathVariable String name);

	@Slf4j
	@Component
	class RiotKrClientFallback implements FallbackFactory<RiotKrClient> {
		@Override
		public RiotKrClient create(Throwable cause) {
			if (cause instanceof FeignException.TooManyRequests) {
				// 429 오류 처리
				log.warn("429 Too Many Requests: {}", cause.getMessage());
				throw new ClientException(ClientErrorCode.TOO_MANY_REQUESTS);
			} else {
				// 그 외의 오류 처리
				log.error("FeignClient 오류: {}", cause.getMessage());
				throw new ClientException(ClientErrorCode.INTERNAL_SERVER_ERROR);
			}
		}
	}

}
