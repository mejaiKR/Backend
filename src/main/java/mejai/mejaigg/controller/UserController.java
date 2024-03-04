package mejai.mejaigg.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import mejai.mejaigg.dto.UserProfileDto;
import mejai.mejaigg.dto.UserStreakDto;

@RestController
@RequiredArgsConstructor
public class UserController {

	@GetMapping("/users/profile")
	public UserProfileDto profile(@RequestParam(value = "id") String id,
		@RequestParam(value = "tag", required = false) String tag) {

		UserProfileDto userProfileDto = new UserProfileDto();
		userProfileDto.setUserId(id);
		userProfileDto.setProfileIcon("profileIcon");
		userProfileDto.setTier("tier");
		userProfileDto.setRank("rank");
		userProfileDto.setLeaguePoints(100);
		userProfileDto.setWins(100);
		userProfileDto.setLosses(100);
		return userProfileDto;
	}

	@GetMapping("/users/streak")
	public List<UserStreakDto> streak(@RequestParam(value = "id") String id,
		@RequestParam(value = "tag", required = false) String tag) {
		UserStreakDto userStreakDto = new UserStreakDto();
		userStreakDto.setDate("2021-01-01");
		userStreakDto.setWinCount(3);
		userStreakDto.setLoseCount(2);
		userStreakDto.setGameCount(5);
		List<UserStreakDto> userStreakDtoList = List.of(userStreakDto);
		return userStreakDtoList;
	}
}
