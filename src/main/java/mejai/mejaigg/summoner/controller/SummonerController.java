package mejai.mejaigg.summoner.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.awspring.cloud.sqs.operations.SendResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mejai.mejaigg.global.cache.CacheNames;
import mejai.mejaigg.matchstreak.service.StreakService;
import mejai.mejaigg.messaging.sqs.sender.MessageSender;
import mejai.mejaigg.searchhistory.dto.RankingRequestDto;
import mejai.mejaigg.searchhistory.dto.RankingResponse;
import mejai.mejaigg.searchhistory.service.SearchHistoryService;
import mejai.mejaigg.summoner.dto.request.SummonerProfileRequest;
import mejai.mejaigg.summoner.dto.request.SummonerSearchRequest;
import mejai.mejaigg.summoner.dto.request.SummonerStreakRequest;
import mejai.mejaigg.summoner.dto.response.RenewalStatusResponse;
import mejai.mejaigg.summoner.dto.response.SummonerProfileResponse;
import mejai.mejaigg.summoner.dto.response.SummonerSearchResponse;
import mejai.mejaigg.summoner.dto.response.SummonerStreakResponse;
import mejai.mejaigg.summoner.service.SummonerService;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = {"https://mejai.kr", "http://localhost:3000", "https://mejai.xyz"},
	methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
@Tag(name = "Summoner", description = "소환사 정보 및 게임 통계 API")
@RequestMapping("/web/summoner")
public class SummonerController {
	private final SummonerService summonerService;
	private final StreakService streakService;
	private final SearchHistoryService searchHistoryService;
	private final MessageSender messageSender;

	@GetMapping("/profile")
	@Operation(summary = "소환사 정보 조회", description = "주어진 소환사 ID로 프로필 정보를 조회 합니다.")
	public SummonerProfileResponse profile(@Valid @ParameterObject SummonerProfileRequest request) {
		return summonerService.getSummonerProfileByNameTag(request.getId(), request.getTag());
	}

	@GetMapping("/profile/renewal")
	@Operation(summary = "소환사 정보 업데이트 상태 조회",
		description = """
			/profile/renewal Post 요청을 보낸 후, 해당 요청의 상태를 조회할 수 있습니다.  \s
			/summoner/profile 요청에서 가져온 lastUpdatedAt 값과 비교하여 최신화 되었으면 업데이트가 완료된 상태입니다.  \s
			최신화 될 때 까지 몇초에 한 번 요청을 보내어 상태를 확인하면 됩니다.  \s
			너무 오랜 기간동안 업데이트가 완료되지 않는다면, 다시 요청을 보내달란 메시지를 보여주세요.  \s
			""")
	public RenewalStatusResponse getProfileRenewalStatus(@Valid @ParameterObject SummonerProfileRequest request) {
		return summonerService.getProfileRenewalStatus(request.getId(), request.getTag());
	}

	@PostMapping("/profile/renewal")
	@Operation(summary = "소환사 정보 업데이트", description = "주어진 소환사 ID로 프로필 정보를 업데이트 요청을 보냅니다.")
	public SendResult<String> renewalProfile(@RequestBody SummonerProfileRequest request) {
		return messageSender.sendMessage(request);
	}

	@GetMapping("/streak")
	@Operation(summary = "소환사 게임 횟수 및 승패 조회", description = "소환사가 특정 기간 동안 진행한 게임 횟수 및 승패를 업데이트 및 조회합니다.")
	public SummonerStreakResponse streak(@Valid @ParameterObject SummonerStreakRequest request) {
		return streakService.getStreak(
			request.getId(),
			request.getTag(),
			request.getYear(),
			request.getMonth()
		);
	}

	@GetMapping("/streak/renewal")
	@Operation(summary = "소환사 게임 횟수 및 승패 업데이트 상태 조회",
		description = """
			/summoner/streak/renewal 요청을 보낸 후, 해당 요청의 상태를 조회할 수 있습니다.  \s
			/summoner/streak 요청에서 가져온 lastUpdatedAt 값과 비교하여 최신화 되었으면 업데이트가 완료된 상태입니다.  \s
			최신화 될 때 까지 몇초에 한 번 요청을 보내어 상태를 확인하면 됩니다.  \s
			너무 오랜 기간동안 업데이트가 완료되지 않는다면, 다시 요청을 보내달란 메시지를 보여주세요.
			""")
	public RenewalStatusResponse renewalStreakStatus(@Valid @ParameterObject SummonerStreakRequest request) {
		return streakService.getStreakRenewalStatus(
			request.getId(),
			request.getTag(),
			request.getYear(),
			request.getMonth()
		);
	}

	@PostMapping("/streak/renewal")
	@Operation(summary = "소환사 게임 횟수 및 승패 업데이트", description = "소환사가 특정 기간 동안 진행한 게임 횟수 및 승패 업데이트 요청을 서버에 전송합니다.")
	public SendResult<String> postRenewalStreak(@RequestBody @Valid SummonerStreakRequest request) {
		return messageSender.sendMessage(request);
	}

	@GetMapping("/ranking")
	@Cacheable(cacheNames = CacheNames.MEJAI_API_CACHE)
	public RankingResponse getRanking(@Valid RankingRequestDto request) {
		return searchHistoryService.getRanking(request.getYear(), request.getMonth());
	}

	@GetMapping("/search")
	@Operation(summary = "소환사 검색", description = """
		검색어와 일치하는 이름을 가진 DB에 존재하는 소환사를 조회합니다.

		사용법:
		검색어를 입력하면, 검색어와 일치하는 이름을 가진 소환사를 count만큼 조회합니다.
		검색어는 소환사 이름의 일부분이어도 상관없습니다.
		""")
	@Cacheable(cacheNames = CacheNames.MEJAI_API_CACHE)
	public SummonerSearchResponse search(@ParameterObject SummonerSearchRequest request) {
		return summonerService.searchSummoner(request.getId(), request.getCount());
	}

}
