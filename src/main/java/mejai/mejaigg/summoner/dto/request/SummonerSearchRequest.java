package mejai.mejaigg.summoner.dto.request;

import lombok.Data;

@Data
public class SummonerSearchRequest {
	private String summonerName;
	private int count = 5;
}
