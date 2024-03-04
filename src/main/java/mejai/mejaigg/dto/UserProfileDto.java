package mejai.mejaigg.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileDto {
	private String userId;
	private String profileIcon;
	private String tier;
	private String rank;
	private int leaguePoints;
	private int wins;
	private int losses;
}
