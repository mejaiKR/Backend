package mejai.mejaigg.match.domain;

import com.fasterxml.jackson.databind.JsonNode;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "match_record")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Match {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "map_id", nullable = false)
	private Integer mapId;

	@Column(name = "queue_id", nullable = false)
	private Integer queueId;

	@Column(name = "game_creation")
	private LocalDateTime gameCreation;

	@Column(name = "game_duration")
	private Long gameDuration;

	@Column(name = "game_end_timestamp")
	private LocalDateTime gameEndTimestamp;

	@Column(name = "game_id")
	private Long gameId;

	@Column(name = "game_start_timestamp")
	private LocalDateTime gameStartTimestamp;

	@Column(name = "game_mode", length = 255)
	private String gameMode;

	@Column(name = "game_name", length = 255)
	private String gameName;

	@Column(name = "game_type", length = 255)
	private String gameType;

	@Column(name = "game_version", length = 255)
	private String gameVersion;

	@Column(name = "match_id", nullable = false, length = 255)
	private String matchId;

	@Column(name = "platform_id", length = 255)
	private String platformId;

	@Column(name = "tournament_code", length = 255)
	private String tournamentCode;

	@Basic(fetch = FetchType.LAZY)
	@Type(JsonType.class)
	@Column(name = "api_response_data", columnDefinition = "jsonb")
	private JsonNode apiResponseData;

	public static Match parseJsonData(JsonNode matchData, JsonNode metadataData, JsonNode infoData) {
		return Match.builder()
			.mapId(infoData.get("mapId").asInt())
			.queueId(infoData.get("queueId").asInt())
			.gameCreation(LocalDateTime.ofInstant(Instant.ofEpochMilli(infoData.get("gameCreation").asLong()), ZoneId.systemDefault()))
			.gameDuration(infoData.get("gameDuration").asLong())
			.gameEndTimestamp(LocalDateTime.ofInstant(Instant.ofEpochMilli(infoData.get("gameEndTimestamp").asLong()), ZoneId.systemDefault()))
			.gameId(infoData.get("gameId").asLong())
			.gameStartTimestamp(LocalDateTime.ofInstant(Instant.ofEpochMilli(infoData.get("gameStartTimestamp").asLong()), ZoneId.systemDefault()))
			.gameMode(infoData.get("gameMode").asText())
			.gameName(infoData.get("gameName").asText())
			.gameType(infoData.get("gameType").asText())
			.gameVersion(infoData.get("gameVersion").asText())
			.matchId(metadataData.get("matchId").asText())
			.platformId(infoData.get("platformId").asText())
			.tournamentCode(infoData.get("tournamentCode").asText())
			.apiResponseData(matchData)
			.build();
	}
}
