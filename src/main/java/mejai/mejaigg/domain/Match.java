package mejai.mejaigg.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Match {
	@Id
	String matchId;

	@ManyToOne(fetch = FetchType.LAZY)
	private MatchParticipant matchParticipant;

}
