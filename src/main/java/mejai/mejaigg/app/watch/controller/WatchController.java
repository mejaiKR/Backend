package mejai.mejaigg.app.watch.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
import mejai.mejaigg.app.watch.dto.CreateSummonerResponse;
import mejai.mejaigg.app.watch.dto.SearchSummonerResponse;
import mejai.mejaigg.app.watch.dto.SummonerRequest;
import mejai.mejaigg.app.watch.dto.WatchSummonerRequest;
import mejai.mejaigg.app.watch.dto.watch.WatchSummonerResponse;
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

			관계 목록:
			애인, 친구, 동료, 자녀, 라이벌, 스트리머
		""")
	@JwtAuth
	public CreateSummonerResponse putWatchSummoner(@RequestAttribute("id") Long userId,
		@RequestBody WatchSummonerRequest request) {
		return watchService.watchSummoner(userId,
			request.getSummonerName(),
			request.getTag(),
			request.getRelationship());
	}

	@GetMapping("/summoner")
	@Operation(summary = "소환사 감시 조회", description = "소환사 감시 정보를 조회합니다.")
	@JwtAuth
	public WatchSummonerResponse getWatchSummoner(@RequestAttribute("id") Long userId) {
		return watchService.getSummonerRecord(userId);
	}

	@GetMapping("/summoner/search")
	@Operation(summary = "소환사 검색", description = "소환사명과 태그로 소환사를 검색합니다.")
	public SearchSummonerResponse getSearchSummoner(SummonerRequest request) {
		return watchService.getSummoner(request.getSummonerName(), request.getTag());
	}
}
