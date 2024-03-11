package mejai.mejaigg.dto.riot.match;


import lombok.Data;

@Data
public class MetaDataDto {
	private String dataVersion;
	private String matchId;
	private String[] participants;
}
