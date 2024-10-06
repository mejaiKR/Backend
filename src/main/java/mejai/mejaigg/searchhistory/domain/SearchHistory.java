package mejai.mejaigg.searchhistory.domain;

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
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mejai.mejaigg.global.jpa.BaseEntity;
import mejai.mejaigg.matchstreak.domain.MatchStreak;
import mejai.mejaigg.summoner.domain.Summoner;

@Getter
@Builder
@Entity
@Table(name = "search_history")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchHistory extends BaseEntity {
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;

	@Column(name = "done", nullable = false)
	@ColumnDefault("false")
	@Setter
	private boolean done;

	@Column(name = "year_month", nullable = false)
	private YearMonth date; // YYYY-MM 형식

	@Column(name = "last_success_day", nullable = false)
	@ColumnDefault("0")
	private int lastSuccessDay; // 마지막으로 api 콜이 성공한 날짜.

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "summoner_id")
	private Summoner summoner;

	@OneToMany(mappedBy = "searchHistory")
	@Builder.Default
	private Set<MatchStreak> matchStreaks = new HashSet<>();

	public void setYearMonthAndUser(YearMonth yearMonth, Summoner summoner) {
		this.date = yearMonth;
		this.summoner = summoner;
		summoner.addSearchHistory(this);
	}

	public void addMatchDateStreak(MatchStreak matchStreak) {
		this.matchStreaks.add(matchStreak);
		matchStreak.setSearchHistory(this);
	}

	public Set<MatchStreak> getSortedMatchDateStreaks() {
		return new TreeSet<>(matchStreaks);
	}
}

