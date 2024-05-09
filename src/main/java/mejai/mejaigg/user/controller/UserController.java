package mejai.mejaigg.user.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mejai.mejaigg.riot.exception.ClientErrorCode;
import mejai.mejaigg.riot.exception.ClientException;
import mejai.mejaigg.user.dto.request.UserProfileRequest;
import mejai.mejaigg.user.dto.request.UserStreakRequest;
import mejai.mejaigg.user.dto.response.UserProfileDto;
import mejai.mejaigg.user.dto.response.UserStreakDto;
import mejai.mejaigg.user.service.UserService;
import mejai.mejaigg.user.service.UserStreakService;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = {"https://mejai.kr", "http://localhost:3000"})
@Tag(name = "Users", description = "소환사 정보 및 게임 통계 API")
public class UserController {

	private final UserService userService;
	private final UserStreakService userStreakService;

	@GetMapping("/users/profile")
	@Operation(summary = "소환사 정보 조회", description = "주어진 소환사 ID로 프로필 정보를 조회합니다.")
	public UserProfileDto profile(UserProfileRequest request) {
		return userService.getUserProfileByNameTag(request.getId(), request.getTag());
	}

	@GetMapping("/users/streak")
	@Operation(summary = "소환사 게임 횟수 및 승패 조회", description = "소환사가 특정 기간 동안 진행한 게임 횟수 및 승패를 조회합니다.")
	public List<UserStreakDto> streak(@Valid UserStreakRequest request) {

		try {
			Optional<List<UserStreakDto>> userMonthStreak = userStreakService.getUserMonthStreak(request);
			return userMonthStreak.get();
		} catch (ClientException e) {
			if (e.getClientErrorCode() == ClientErrorCode.INTERNAL_SERVER_ERROR) {
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Roit 서버 에러");
			} else if (e.getClientErrorCode() == ClientErrorCode.TOO_MANY_REQUESTS) {
				throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
					"Too many requests. Please try again later.");
			}
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "소환사를 찾을 수 없습니다.");
	}
}
