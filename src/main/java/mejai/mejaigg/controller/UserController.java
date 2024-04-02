package mejai.mejaigg.controller;

import java.util.Set;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public Set<UserStreakDto> streak(@Valid UserStreakRequest request) {
		String puuid = userService.getPuuidByNameTag(request.getId(), request.getTag());

		Set<UserStreakDto> userMonthStreak = userService.getUserMonthStreak(puuid, request.getYear(),
			request.getMonth());
		// List<UserStreakDto> userStreakDtos = new ArrayList<>();
		// int maxDay = YearMonthToEpochUtil.findMaxDay(year, month);
		// for (int i = 1; i <= maxDay; i++) {
		// 	UserStreakDto userStreakDto = new UserStreakDto();
		// 	userStreakDto.setDummy(year, month, i);
		// 	userStreakDtos.add(userStreakDto);
		// }
		return userMonthStreak;
	}
}
