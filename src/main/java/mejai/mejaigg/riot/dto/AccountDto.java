package mejai.mejaigg.riot.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountDto {
	private String puuid;
	private String gameName;
	private String tagLine;
}
