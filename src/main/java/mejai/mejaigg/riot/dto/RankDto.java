package mejai.mejaigg.riot.dto;

import lombok.Data;

@Data
public class RankDto {
	private String leagueId;
	private String queueType;
	private String tier;
	private String rank;
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
