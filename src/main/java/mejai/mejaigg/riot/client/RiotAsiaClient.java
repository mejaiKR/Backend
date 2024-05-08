package mejai.mejaigg.riot.client;

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
import mejai.mejaigg.riot.dto.AccountDto;
import mejai.mejaigg.riot.dto.match.MatchDto;

@FeignClient(
	name = "RiotAsiaClient",
	url = "https://asia.api.riotgames.com",
	fallback = RiotAsiaClient.RiotAsiaClientFallback.class
)
public interface RiotAsiaClient {

	@Retryable(
		value = {ServerErrorException.class}, // 재시도할 예외 지정
		maxAttempts = 3, // 최대 재시도 횟수
		backoff = @Backoff(delay = 2000) // 재시도 간격
	)
	@GetMapping("/riot/account/v1/accounts/by-riot-id/{summonerName}/{tag}")
	AccountDto getAccountByNameAndTag(@RequestParam("api_key") String riotKey, @PathVariable String summonerName,
		@PathVariable String tag);

	@Retryable(
		value = {ServerErrorException.class}, // 재시도할 예외 지정
		maxAttempts = 3, // 최대 재시도 횟수
		backoff = @Backoff(delay = 2000) // 재시도 간격
	)
	@GetMapping("/lol/match/v5/matches/by-puuid/{puuid}/ids")
	String[] getMatchHistoryByPuuid(@RequestParam("api_key") String riotKey, @PathVariable String puuid,
		@RequestParam("startTime") Long startTime,
		@RequestParam("endTime") Long endTime, @RequestParam("start") Long start,
		@RequestParam("count") int count);

	@Retryable(
		value = {ServerErrorException.class}, // 재시도할 예외 지정
		maxAttempts = 3, // 최대 재시도 횟수
		backoff = @Backoff(delay = 2000) // 재시도 간격
	)
	@GetMapping("/lol/match/v5/matches/{matchId}")
	MatchDto getMatchDtoByMatchId(@RequestParam("api_key") String riotKey, @PathVariable String matchId);

	@Slf4j
	@Component
	class RiotAsiaClientFallback implements FallbackFactory<RiotAsiaClient> {
		@Override
		public RiotAsiaClient create(Throwable cause) {
			if (cause instanceof FeignException.TooManyRequests) {
				log.warn("429 Too Many Requests: {}", cause.getMessage());
				throw new ClientException(ClientErrorCode.TOO_MANY_REQUESTS);
			} else {
				log.error("Riot Asia Client 오류: {}", cause.getMessage());
				throw new ClientException(ClientErrorCode.INTERNAL_SERVER_ERROR);
			}
		}
	}
}
