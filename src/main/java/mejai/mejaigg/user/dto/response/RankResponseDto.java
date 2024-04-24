package mejai.mejaigg.user.dto.response;

import lombok.Data;
import mejai.mejaigg.rank.entity.Rank;

@Data
public class RankResponseDto {
	private String queueType;
	private String tier;
	private String tierIcon;
	private String rank;
	private Long leaguePoints;
	private int wins;
	private int losses;

	public void setByRank(Rank rank, String resourceUrl) {
		this.queueType = rank.getQueueType();
		this.tier = rank.getTier();
		this.tierIcon = resourceUrl + "emblem/" + rank.getTier() + ".png";
		this.rank = rank.getRank();
		this.leaguePoints = rank.getLeaguePoints();
		this.wins = rank.getWins();
		this.losses = rank.getLosses();
	}
}
