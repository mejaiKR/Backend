package mejai.mejaigg.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class SearchHistory {
	@Id
	@GeneratedValue
	private Long historyId;

	boolean isDone;

	@Column(length = 6)
	private String yearMonth; // YYYYMM 형식

	@ManyToOne
	private User user;

	@OneToMany
	private Set<MatchDateStreak> matchDateStreaks = new HashSet<>();
}
