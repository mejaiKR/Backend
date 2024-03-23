package mejai.mejaigg.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
	private String tournamentCode;

	@OneToMany(mappedBy = "game")
	private Set<MatchParticipant> matchParticipants = new HashSet<>();

	@OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
	private List<UserGameStat> gameStats = new ArrayList<>();

	public void addMatchParticipant(MatchParticipant matchParticipant) {
		matchParticipants.add(matchParticipant);
		matchParticipant.setGame(this);
	}

	public void setGameStats(List<UserGameStat> gameStats) {
		this.gameStats = gameStats;
	}

	public void addGameStat(UserGameStat userGameStat) {
		gameStats.add(userGameStat);
		userGameStat.setGame(this);
	}

	public void setMatchId(String matchId) {
		this.matchId = matchId;
	}
}
