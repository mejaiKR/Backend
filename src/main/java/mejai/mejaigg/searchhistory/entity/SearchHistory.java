package mejai.mejaigg.searchhistory.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import mejai.mejaigg.global.jpa.BaseEntity;
import mejai.mejaigg.matchdatestreak.entity.MatchDateStreak;
import mejai.mejaigg.summoner.entity.User;

@Entity
@Getter
public class SearchHistory extends BaseEntity {
	@Id
	@GeneratedValue
	private Long historyId;

	private boolean isDone = false;

	@Column(length = 7)
	private String yearMonth; // YYYY-MM 형식

	private int lastSuccessDay = 0; // 마지막으로 api 콜이 성공한 날짜.

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
		matchDateStreak.setSearchHistory(this);
	}

	public Set<MatchDateStreak> getSortedMatchDateStreaks() {
		return new TreeSet<>(matchDateStreaks);
	}
}
