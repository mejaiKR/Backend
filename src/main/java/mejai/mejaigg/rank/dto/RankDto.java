package mejai.mejaigg.rank.dto;

import lombok.Data;
import mejai.mejaigg.rank.domain.RankType;
import mejai.mejaigg.rank.domain.TierType;

@Data
public class RankDto {
	private String leagueId;
	private String queueType;
	private TierType tier;
	private RankType rank;
	private String summonerId;
	private String summonerName;
	private Long leaguePoints;
	private int wins;
	private int losses;
	private boolean veteran;
	private boolean inactive;
	private boolean freshBlood;
	private boolean hotStreak;
}
