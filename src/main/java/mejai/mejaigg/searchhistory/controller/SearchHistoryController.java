package mejai.mejaigg.searchhistory.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mejai.mejaigg.searchhistory.Service.SearchHistoryService;
import mejai.mejaigg.searchhistory.dto.RankingRequestDto;
import mejai.mejaigg.searchhistory.dto.SearchRankingDto;

@Tag(name = "Users", description = "소환사 정보 및 게임 통계 API")
@RestController
@CrossOrigin(origins = "http://localhost:3000, https://mejai.gg")
@RequiredArgsConstructor
public class SearchHistoryController {

	private final SearchHistoryService searchHistoryService;
	@GetMapping("/ranking")
	public SearchRankingDto searchRanking(@Valid RankingRequestDto request) {
		return searchHistoryService.getRanking(request.getYear(), request.getMonth());
	}
}