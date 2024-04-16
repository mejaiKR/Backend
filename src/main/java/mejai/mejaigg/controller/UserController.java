package mejai.mejaigg.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mejai.mejaigg.dto.request.UserProfileRequest;
import mejai.mejaigg.dto.request.UserStreakRequest;
import mejai.mejaigg.dto.response.UserProfileDto;
import mejai.mejaigg.dto.response.UserStreakDto;
import mejai.mejaigg.service.UserService;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

	private final UserService userService;

	@GetMapping("/users/profile")
	public UserProfileDto profile(UserProfileRequest request) {
		return userService.getUserProfileByNameTag(request.getId(), request.getTag());
	}

	@GetMapping("/users/streak")
	public List<UserStreakDto> streak(@Valid UserStreakRequest request) {
		String puuid = userService.getPuuidByNameTag(request.getId(), request.getTag());

		Optional<List<UserStreakDto>> userMonthStreak = userService.getUserMonthStreak(puuid, request.getYear(),
			request.getMonth());
		if (userMonthStreak.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
				"Too many requests. Please try again later.");
		}
		return userMonthStreak.get();
	}
}
