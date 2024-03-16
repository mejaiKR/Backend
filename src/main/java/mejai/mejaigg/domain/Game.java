package mejai.mejaigg.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;
import lombok.Getter;
import mejai.mejaigg.dto.riot.match.MatchDto;

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

	@OneToMany(mappedBy = "game")
	private List<UserGameStat> gameStats = new ArrayList<>();

	public void setByMatchDto(MatchDto matchDto){
		this.matchId = matchDto.getMetadata().getMatchId();
		this.gameCreation = matchDto.getInfo().getGameCreation();
		this.gameDuration = matchDto.getInfo().getGameDuration();
		this.gameEndTimestamp = matchDto.getInfo().getGameEndTimestamp();
		this.gameId = matchDto.getInfo().getGameId();
		this.gameMode = matchDto.getInfo().getGameMode();
		this.gameName = matchDto.getInfo().getGameName();
		this.gameStartTimestamp = matchDto.getInfo().getGameStartTimestamp();
		this.gameType = matchDto.getInfo().getGameType();
		this.gameVersion = matchDto.getInfo().getGameVersion();
		this.mapId = matchDto.getInfo().getMapId();
		this.platformId = matchDto.getInfo().getPlatformId();
		this.queueId = matchDto.getInfo().getQueueId();
		this.tournamentCode = matchDto.getInfo().getTournamentCode();
	}

	public void addMatchParticipant(MatchParticipant matchParticipant){
		matchParticipants.add(matchParticipant);
		matchParticipant.setGame(this);
	}

	public void addGameStat(UserGameStat userGameStat){
		gameStats.add(userGameStat);
		userGameStat.setGame(this);
	}
}
