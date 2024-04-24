package mejai.mejaigg.riot.dto.match;

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
	String platformId;
	int queueId;
	ParticipantDto[] participants;
	String tournamentCode;
}
