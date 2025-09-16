package kr.mejai.project.external.riot.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SummonerDto {
	private int profileIconId;
	private Long revisionDate;
	private String puuid;
	private Long summonerLevel;
}
