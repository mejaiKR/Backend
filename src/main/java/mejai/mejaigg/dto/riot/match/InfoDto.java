package mejai.mejaigg.dto.riot.match;

import lombok.Data;

@Data
public class InfoDto {
	String endOfGameResult;
	Long gameCreation;
	Long gameDuration;
	Long gameEndTimestamp;
	Long gameId;
	String gameMode;
	String gameName;
	Long gameStartTimestamp;
	String gameType;
	String gameVersion;
	int mapId;
	ParticipantDto[] participants;
}
