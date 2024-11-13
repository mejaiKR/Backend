package mejai.mejaigg.match_participant.domain;

import com.fasterxml.jackson.databind.JsonNode;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mejai.mejaigg.match.domain.Match;
import org.hibernate.annotations.Type;

@Entity
@Getter
@Builder
@Table(name = "match_participant")
@NoArgsConstructor
@AllArgsConstructor
public class MatchParticipant {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "match_participant_id_seq")
	@SequenceGenerator(name = "match_participant_id_seq", sequenceName = "match_participant_id_seq", allocationSize = 1)
	private Long id;

	@Column(name = "match_id", nullable = false)
	private Long matchId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "match_id", nullable = false, insertable = false, updatable = false)
	private Match match;

	@Column(name = "puuid", length = 255)
	private String puuid;

	@Column(name = "summoner_id", length = 255)
	private String summonerId;

	@Column(name = "team_id")
	private Integer teamId;

	@Column(name = "time_played")
	private Integer timePlayed;

	@Column(name = "kills")
	private Integer kills;

	@Column(name = "assists")
	private Integer assists;

	@Column(name = "deaths")
	private Integer deaths;

	@Column(name = "champion_id")
	private Integer championId;

	@Column(name = "champion_name")
	private String championName;

	@Column(name = "total_minions_killed")
	private Integer totalMinionsKilled;

	@Column(name = "champ_level")
	private Integer champLevel;

	@Column(name = "vision_score")
	private Integer visionScore;

	@Column(name = "win")
	private Boolean win;

	@Basic(fetch = FetchType.LAZY)
	@Type(JsonType.class)
	@Column(name = "api_response_data", columnDefinition = "jsonb")
	private JsonNode apiResponseData;

	public static MatchParticipant parseJsonData(JsonNode participantData, Long matchId) {
		return MatchParticipant.builder()
			.matchId(matchId)
			.puuid(participantData.get("puuid").asText())
			.summonerId(participantData.get("summonerId").asText())
			.teamId(participantData.get("teamId").asInt())
			.timePlayed(participantData.get("timePlayed").asInt())
			.kills(participantData.get("kills").asInt())
			.assists(participantData.get("assists").asInt())
			.deaths(participantData.get("deaths").asInt())
			.championId(participantData.get("championId").asInt())
			.championName(participantData.get("championName").asText())
			.totalMinionsKilled(participantData.get("totalMinionsKilled").asInt())
			.champLevel(participantData.get("champLevel").asInt())
			.visionScore(participantData.get("visionScore").asInt())
			.win(participantData.get("win").asBoolean())
			.apiResponseData(participantData)
			.build();
	}

}
