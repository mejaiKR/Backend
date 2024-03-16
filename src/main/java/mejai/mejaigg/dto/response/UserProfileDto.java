package mejai.mejaigg.dto.response;

import lombok.Getter;
import lombok.Setter;
import mejai.mejaigg.domain.Rank;
import mejai.mejaigg.domain.User;

@Getter
@Setter
public class UserProfileDto {
	private String userId;
	private int profileIcon;
	private String tier;
	private int rank;
	private Long leaguePoints;
	private int wins;
	private int losses;

	public void setByUser(User user){
		Rank rank = user.getRanks().stream().findFirst().orElse(null);
		this.userId = user.getPuuid();
		this.profileIcon = user.getProfileIconId();
		this.tier = rank.getTier();
		this.rank = rank.getRank();
		this.leaguePoints = rank.getLeaguePoints();
		this.wins = rank.getWins();
		this.losses = rank.getLosses();
	}
}
