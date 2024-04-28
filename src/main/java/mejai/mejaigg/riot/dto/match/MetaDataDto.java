package mejai.mejaigg.riot.dto.match;

import lombok.Data;

@Data
public class MetaDataDto {
	private String dataVersion;
	private String matchId;
	private String[] participants;
}
