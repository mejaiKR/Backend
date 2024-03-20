package mejai.mejaigg.dto.response;

import lombok.Getter;
import lombok.Setter;
import mejai.mejaigg.common.RomanNumber;
import mejai.mejaigg.domain.Rank;
import mejai.mejaigg.domain.User;

@Getter
@Setter
public class UserProfileDto {
	private String userId;
	private String profileIcon;
	private String tier;
	private String rank;
	private Long leaguePoints;
	private int wins;
	private int losses;

	public void setByUser(User user, String requestUrl){
		Rank rank = user.getRanks().stream().findFirst().orElse(null);
		this.userId = user.getPuuid();
		this.profileIcon = requestUrl + user.getProfileIconId() +".png";
		this.tier = rank.getTier();
		this.rank = RomanNumber.toRoman(rank.getRank());
		this.leaguePoints = rank.getLeaguePoints();
		this.wins = rank.getWins();
		this.losses = rank.getLosses();
	}
}
