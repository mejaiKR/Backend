package mejai.mejaigg.match.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import mejai.mejaigg.match.game.entity.Game;
import mejai.mejaigg.match.matchdatestreak.entity.MatchDateStreak;

@Entity
@Getter
public class Match {
	@Id
	String matchId;

	@ManyToOne(fetch = FetchType.LAZY)
	private MatchDateStreak matchDateStreak;

	private boolean isCalled = false;

	@OneToOne(mappedBy = "match", cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn
	private Game game;

	public Match() {
	}
}
