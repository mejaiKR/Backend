package mejai.mejaigg.rank.dto;

import lombok.Data;
import mejai.mejaigg.rank.domain.RankType;
import mejai.mejaigg.rank.domain.TierType;

@Data
public class RankDto {
	private String leagueId;
	private String puuid;
	private String queueType;
	private TierType tier;
	private RankType rank;
	private Long leaguePoints;
	private int wins;
	private int losses;
	private boolean hotStreak;
	private boolean veteran;
	private boolean freshBlood;
	private boolean inactive;
	private MiniSeriesDto miniSeries;
}
