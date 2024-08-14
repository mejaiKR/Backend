package mejai.mejaigg.searchhistory.entity;

import java.time.YearMonth;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
	@Column(name = "search_history_id")
	private Long id;

	@Column(name = "done", nullable = false)
	@ColumnDefault("false")
	private boolean done;

	@Column(name = "history_date", nullable = false)
	private YearMonth date; // YYYY-MM 형식

	@Column(name = "last_success_day", nullable = false)
	@ColumnDefault("0")
	private int lastSuccessDay; // 마지막으로 api 콜이 성공한 날짜.

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn()
	private User user;

	@OneToMany(mappedBy = "searchHistory")
	private Set<MatchDateStreak> matchDateStreaks = new HashSet<>();

	public void setYearMonthAndUser(YearMonth yearMonth, User user) {
		this.date = yearMonth;
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
