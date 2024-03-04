package mejai.mejaigg.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.Getter;

@Entity
@Getter
public class Game {
	@Id
	private String matchId;
	private Long gameCreation;
	private Long gameDuration;
	private Long gameEndTimestamp;
	private Long gameId;
	private String gameMode;
	private String gameName;
	private Long gameStartTimestamp;
	private String gameType;
	private String gameVersion;
	private int mapId;
	private String platformId;
	private int queueId;
	private int tournamentCode;

	@OneToMany(mappedBy = "game")
	private Set<MatchParticipant> matchParticipants = new HashSet<>();

	@OneToMany
	@JoinColumn(name = "user_game_stat_puuid")
	private List<UserGameStat> gameStats = new ArrayList<>();

}
