package mejai.mejaigg.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import mejai.mejaigg.common.YearMonthToEpochUtil;
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
		// return userService.getUserProfileByNameTag(id, tag);
		UserProfileDto userProfileDto = new UserProfileDto();
		userProfileDto.setDummy();
		return userProfileDto;
	}

	@GetMapping("/users/streak")
	public List<UserStreakDto> streak(@RequestParam(value = "id") String id,
		@RequestParam(value = "tag", required = false, defaultValue = "Kr1") String tag,
		@RequestParam(value = "year") int year, @RequestParam(value = "month") int month) {
		// String puuid = userService.getPuuidByNameTag(id, tag);
		// Set<UserStreakDto> userMonthStreak = userService.getUserMonthStreak(puuid, dateYM);
		List<UserStreakDto> userStreakDtos = new ArrayList<>();
		int maxDay = YearMonthToEpochUtil.findMaxDay(year, month);
		for (int i = 1; i <= maxDay; i++) {
			UserStreakDto userStreakDto = new UserStreakDto();
			userStreakDto.setDummy(year, month, i);
			userStreakDtos.add(userStreakDto);
		}
		return userStreakDtos;
	}
}
