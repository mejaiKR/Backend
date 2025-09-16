package kr.mejai.project.domain.summoner.dto.response;

import kr.mejai.project.domain.rank.domain.Rank;
import kr.mejai.project.domain.rank.domain.RankType;
import kr.mejai.project.domain.rank.domain.TierType;
import lombok.Data;

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
