package mejai.mejaigg.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity
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

	public Match(String matchId, boolean isCalled) {
		this.matchId = matchId;
		this.isCalled = isCalled;
	}

}
