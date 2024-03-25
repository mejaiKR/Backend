package mejai.mejaigg.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;

@Entity
@Getter
public class MatchParticipant {

	@Id
	private String matchId;

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "match_count_id", referencedColumnName = "id")
	private MatchCount matchCount;

	public void setUser(User user) {
		this.user = user;
	}
}
