package mejai.mejaigg.dto.response;

import lombok.Getter;
import lombok.Setter;
import mejai.mejaigg.domain.Rank;
import mejai.mejaigg.domain.User;

@Getter
@Setter
public class UserProfileDto {
	private String userName;
	private String tagLine;
	private String profileIcon;
	private String tierIcon;
	private String tier;
	private String rank;
	private Long leaguePoints;
	private Long level;
	private int wins;
	private int losses;

	public void setByUser(User user, String resourceUrl) {
		Rank rank = user.getRank();
		this.userName = user.getSummonerName();
		this.profileIcon = resourceUrl + "profileIcon/image" + user.getProfileIconId() + ".png";
		this.tier = rank.getTier();
		this.tierIcon = resourceUrl + "emblem/" + rank.getTier() + ".png";
		this.rank = user.getRank().getRank();
		this.leaguePoints = rank.getLeaguePoints();
		this.wins = rank.getWins();
		this.losses = rank.getLosses();
	}

	public void setDummy() {
		this.userName = "hide on bush";
		this.profileIcon = "http://localhost:8080/profileIcon/image6.png";
		this.tierIcon = "http://localhost:8080/emblem/Challenger.png";
		this.tier = "CHALLENGER";
		this.rank = "I";
		this.leaguePoints = 704L;
		this.wins = 220;
		this.losses = 184;
		this.level = 745L;
		this.tagLine = "KR1";
	}
}
