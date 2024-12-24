package mejai.mejaigg.summoner.dto.response;

import lombok.Data;
import mejai.mejaigg.rank.domain.Rank;
import mejai.mejaigg.rank.domain.RankType;
import mejai.mejaigg.rank.domain.TierType;

@Data
public class RankResponse {
	private TierType tier;
	private String tierIcon;
	private RankType rank;
	private Long leaguePoints;
	private int wins;
	private int losses;

	public RankResponse(Rank rank, String resourceUrl) {
		this.tier = rank.getTier();
		this.tierIcon = resourceUrl + "emblem/" + rank.getTier() + ".png";
		this.rank = rank.getRank();
		this.leaguePoints = rank.getLeaguePoints();
		this.wins = rank.getWins();
		this.losses = rank.getLosses();
	}
}
