package mejai.mejaigg.matchDateStreak.entity;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import mejai.mejaigg.common.jpa.BaseEntity;
import mejai.mejaigg.match.entity.Match;
import mejai.mejaigg.searchHistory.entity.SearchHistory;

@Entity
@Getter
@Setter
public class MatchDateStreak extends BaseEntity implements Comparable<MatchDateStreak> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // ID 자동 생성 전략 사용
	private Long id;

	private Date date; // yyyy-MM-dd

	private int allGameCount;
	private int rankGameCount;

	@OneToMany(mappedBy = "matchDateStreak", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Match> matches = new HashSet<>();

	@ManyToOne(fetch = FetchType.LAZY)
	private SearchHistory searchHistory;

	@Override
	public int compareTo(MatchDateStreak st) {
		return this.date.compareTo(st.date);
	}

	public void addMatch(Match match) {
		matches.add(match);
		match.setMatchDateStreak(this);
	}
}
