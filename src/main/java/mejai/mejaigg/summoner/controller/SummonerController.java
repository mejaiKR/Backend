package mejai.mejaigg.summoner.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mejai.mejaigg.matchstreak.service.StreakService;
import mejai.mejaigg.searchhistory.dto.RankingRequestDto;
import mejai.mejaigg.searchhistory.dto.SearchRankingDto;
import mejai.mejaigg.searchhistory.service.SearchHistoryService;
import mejai.mejaigg.summoner.dto.request.UserProfileRequest;
import mejai.mejaigg.summoner.dto.request.UserStreakRequest;
import mejai.mejaigg.summoner.dto.response.UserProfileDto;
import mejai.mejaigg.summoner.dto.response.UserStreakDto;
import mejai.mejaigg.summoner.service.ProfileService;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = {"https://mejai.kr", "http://localhost:3000", "https://mejai.vercel.app"})
@Tag(name = "Users", description = "소환사 정보 및 게임 통계 API")
public class SummonerController {
	//TODO : 이 컨트롤러는 Production 에는 올라가지 않습니다.
	private final ProfileService profileService;
	private final StreakService streakService;
	private final SearchHistoryService searchHistoryService;

	@GetMapping("/users/profile")
	@Operation(summary = "소환사 정보 조회", description = "주어진 소환사 ID로 프로필 정보를 조회 합니다.")
	public UserProfileDto profile(UserProfileRequest request) {
		return profileService.getUserProfileByNameTag(request.getId(), request.getTag());
	}

	@GetMapping("/users/profile/refresh")
	@Operation(summary = "소환사 정보 업데이트", description = "주어진 소환사 ID로 프로필 정보를 업데이트 합니다.")
	public UserProfileDto refreshProfile(UserProfileRequest request) {
		return profileService.refreshUserProfileByNameTag(request.getId(), request.getTag());
	}

	@GetMapping("/users/streak")
	@Operation(summary = "소환사 게임 횟수 및 승패 조회", description = "소환사가 특정 기간 동안 진행한 게임 횟수 및 승패를 업데이트 및 조회합니다.")
	public List<UserStreakDto> streak(@Valid UserStreakRequest request) {
		return streakService.refreshStreak(request);
	}

	@GetMapping("/users/streak/refresh")
	@Operation(summary = "소환사 게임 횟수 및 승패 업데이트", description = "소환사가 특정 기간 동안 진행한 게임 횟수 및 승패를 업데이트합니다.")
	public List<UserStreakDto> refreshStreak(@Valid UserStreakRequest request) {
		return streakService.refreshStreak(request);
	}

	@GetMapping("/ranking")
	public SearchRankingDto searchRanking(@Valid RankingRequestDto request) {
		return searchHistoryService.getRanking(request.getYear(), request.getMonth());
	}
}
