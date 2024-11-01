package mejai.mejaigg.watch.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mejai.mejaigg.watch.dto.request.SummonerRequest;
import mejai.mejaigg.watch.dto.response.WatchSummonerDto;
import mejai.mejaigg.watch.service.WatchService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = {"https://mejai.kr", "http://localhost:3000", "https://mejai.vercel.app"})
@Tag(name = "watch", description = "소환사 감시 API")
@RequestMapping("/app/watch")
public class WatchController {

	private final WatchService watchService;

	@PostMapping("/summoner")
	@Operation(summary = "소환사 감시 등록", description = "소환사를 감시합니다.")
	public WatchSummonerDto postWatchSummoner(SummonerRequest request) {
		return watchService.watchSummoner(request.getSummonerName(), request.getTag());
	}
}
