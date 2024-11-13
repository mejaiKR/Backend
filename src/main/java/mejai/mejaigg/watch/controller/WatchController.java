package mejai.mejaigg.watch.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mejai.mejaigg.watch.dto.PostWatchSummonerDto;
import mejai.mejaigg.watch.dto.SummonerRequest;
import mejai.mejaigg.watch.dto.watch_summoner.GetWatchSummonerDto;
import mejai.mejaigg.watch.service.WatchService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = {"https://mejai.kr", "http://localhost:3000", "https://mejai.vercel.app"})
@Tag(name = "watch", description = "소환사 감시 API")
@RequestMapping("/app/watch")
public class WatchController {

	private final WatchService watchService;

	@PostMapping("/summoner")
	@Operation(summary = "소환사 감시 등록", description = "소환사를 감시합니다.")
	public PostWatchSummonerDto postWatchSummoner(SummonerRequest request) {
		return watchService.watchSummoner(request.getSummonerName(), request.getTag());
	}

	@GetMapping("/summoner/{id}")
	@Operation(summary = "소환사 감시 조회", description = "소환사 감시 정보를 조회합니다.")
	public GetWatchSummonerDto getWatchSummoner(@PathVariable Long id) {
		return watchService.getSummonerRecord(id);
	}
}
