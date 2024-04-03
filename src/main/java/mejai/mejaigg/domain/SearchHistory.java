package mejai.mejaigg.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;

@Entity
@Getter
public class SearchHistory {
	@Id
	@GeneratedValue
	private Long historyId;

	private boolean isDone = false;

	@Column(length = 7)
	private String yearMonth; // YYYY-MM 형식

	@ManyToOne
	private User user;

	@OneToMany
	private Set<MatchDateStreak> matchDateStreaks = new HashSet<>();

	public void setYearMonthAndUser(String yearMonth, User user) {
		this.yearMonth = yearMonth;
		this.user = user;
		user.addSearchHistory(this);
	}

	public void addMatchDateStreak(MatchDateStreak matchDateStreak) {
		this.matchDateStreaks.add(matchDateStreak);
	}
}
