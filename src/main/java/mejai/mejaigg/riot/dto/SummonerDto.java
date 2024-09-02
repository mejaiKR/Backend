package mejai.mejaigg.riot.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SummonerDto {
	private String accountId;
	private int profileIconId;
	private Long revisionDate;
	private String id;
	private String puuid;
	private Long summonerLevel;
}
