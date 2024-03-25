package mejai.mejaigg.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import mejai.mejaigg.dto.response.UserProfileDto;
import mejai.mejaigg.dto.response.UserStreakDto;
import mejai.mejaigg.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping("/users/profile")
	public UserProfileDto profile(@RequestParam(value = "id") String id,
		@RequestParam(value = "tag", required = false, defaultValue = "Kr1") String tag) {
		return userService.getUserProfileByNameTag(id, tag);
	}

	@GetMapping("/users/streak")
	public List<UserStreakDto> streak(@RequestParam(value = "id") String id,
		@RequestParam(value = "tag", required = false, defaultValue = "Kr1") String tag,
		@RequestParam(value = "dateYM") String dateYM) {
		String puuid = userService.getPuuidByNameTag(id, tag);
		userService.getUserMonthStreak(puuid, dateYM);
		UserStreakDto userStreakDto = new UserStreakDto();
		userStreakDto.setDate("2021-01-01");
		userStreakDto.setWinCount(3);
		userStreakDto.setLoseCount(2);
		userStreakDto.setGameCount(5);
		List<UserStreakDto> userStreakDtoList = List.of(userStreakDto);
		return userStreakDtoList;
	}
}
