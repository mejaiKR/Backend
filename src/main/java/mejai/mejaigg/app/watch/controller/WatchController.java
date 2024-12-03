package mejai.mejaigg.app.watch.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mejai.mejaigg.app.jwt.JwtAuth;
import mejai.mejaigg.app.watch.dto.request.GetWatchSummonerRequest;
import mejai.mejaigg.app.watch.dto.request.PutWatchSummonerRequest;
import mejai.mejaigg.app.watch.dto.request.SummonerRequest;
import mejai.mejaigg.app.watch.dto.response.CreateSummonerResponse;
import mejai.mejaigg.app.watch.dto.response.SearchSummonerResponse;
import mejai.mejaigg.app.watch.dto.response.WatchSummonerDetailsResponse;
import mejai.mejaigg.app.watch.dto.response.watch.WatchSummonerResponse;
import mejai.mejaigg.app.watch.service.WatchService;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = {"*"}, methods = {RequestMethod.GET, RequestMethod.PUT, RequestMethod.OPTIONS})
@Tag(name = "watch", description = "소환사 감시 API")
@RequestMapping("/app/watch")
public class WatchController {

	private final WatchService watchService;

	@PutMapping("/summoner")
	@Operation(summary = "소환사 감시 등록", description = """
			소환사를 감시합니다.
			소환사명과 태그, 관계를 입력하세요.
			해당 소환사의 최근 100개의 전적을 불러옵니다.

			관계 목록:
			애인, 친구, 동료, 자녀, 라이벌, 스트리머
		""")
	@JwtAuth
	public CreateSummonerResponse putWatchSummoner(
		@RequestAttribute("id") Long userId,
		@RequestBody PutWatchSummonerRequest request
	) {
		return watchService.watchSummoner(
			userId,
			request.getSummonerName(),
			request.getTag(),
			request.getRelationship()
		);
	}

	@PostMapping("/summoner/refresh")
	@Operation(summary = "소환사 감시 갱신", description = "소환사 감시 정보를 갱신합니다.")
	@JwtAuth
	public CreateSummonerResponse postRefreshWatchSummoner(@RequestAttribute("id") Long userId) {
		return watchService.refreshWatchSummoner(userId);
	}

	@GetMapping("/summoner")
	@Operation(summary = "소환사 감시 조회", description = """
		감시중인 소환사의 월요일 ~ 일요일 전적을 조회합니다.
		날짜를 입력하지 않으면 오늘을 기준으로 조회합니다.
		날짜를 입력한다면 해당 날짜의 주간 전적을 조회합니다.
		""")
	@JwtAuth
	public WatchSummonerResponse getWatchSummoner(
		@RequestAttribute("id") Long userId,
		@ParameterObject GetWatchSummonerRequest request
	) {
		System.out.println("startDate : " + request.getStartDate());
		return watchService.getSummonerRecord(userId, request.getStartDate());
	}

	@GetMapping("/summoner/detail")
	@Operation(summary = "소환사 감시 상세 조회", description = "감시중인 소환사의 전적을 상세 조회합니다.")
	@JwtAuth
	public WatchSummonerDetailsResponse getWatchSummonerDetails(
		@RequestAttribute("id") Long userId,
		@ParameterObject GetWatchSummonerRequest request
	) {
		return watchService.getSummonerRecordDetail(userId, request.getStartDate());
	}

	@GetMapping("/summoner/search")
	@Operation(summary = "소환사 검색", description = "소환사명과 태그로 소환사를 검색합니다.")
	public SearchSummonerResponse getSearchSummoner(@ParameterObject SummonerRequest request) {
		return watchService.getSummoner(request.getSummonerName(), request.getTag());
	}
}
