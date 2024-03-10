package mejai.mejaigg.dto.riot;

import lombok.Data;

@Data
public class SummonerDTO {
	private String accountId;
	private int profileIconId;
	private long revisionDate;
	private String name;
	private String id;
	private String puuid;
	private long summonerLevel;
}
