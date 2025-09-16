package kr.mejai.project.domain.rank.dto;

import kr.mejai.project.domain.rank.domain.RankType;
import kr.mejai.project.domain.rank.domain.TierType;
import lombok.Data;

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
