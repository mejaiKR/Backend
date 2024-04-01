package mejai.mejaigg.dto.riot;

import lombok.Data;

@Data
public class SummonerDto {
	private String accountId;
	private int profileIconId;
	private Long revisionDate;
	private String name;
	private String id;
	private String puuid;
	private Long summonerLevel;
}
