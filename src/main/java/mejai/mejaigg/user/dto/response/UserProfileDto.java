package mejai.mejaigg.user.dto.response;

import lombok.Getter;
import lombok.Setter;
import mejai.mejaigg.rank.Rank;
import mejai.mejaigg.user.entity.User;

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
		this.profileIcon = resourceUrl + "profileIcon/" + user.getProfileIconId() + ".png";
		this.tier = rank.getTier();
		this.tierIcon = resourceUrl + "emblem/" + rank.getTier() + ".png";
		this.rank = user.getRank().getRank();
		this.leaguePoints = rank.getLeaguePoints();
		this.wins = rank.getWins();
		this.losses = rank.getLosses();
		this.level = user.getSummonerLevel();
		this.tagLine = user.getTagLine();
	}

	public void setDummy() {
		this.userName = "hide on bush";
		this.profileIcon = "http://localhost:8080/profileIcon/6.png";
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
