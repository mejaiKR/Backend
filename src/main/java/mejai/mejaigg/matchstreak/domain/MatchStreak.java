package mejai.mejaigg.matchstreak.domain;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mejai.mejaigg.global.jpa.BaseEntity;
import mejai.mejaigg.searchhistory.domain.SearchHistory;

@Getter
@Builder
@Entity
@Table(name = "match_streak")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchStreak extends BaseEntity implements Comparable<MatchStreak> {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ms_seq")
	@SequenceGenerator(
		name = "ms_seq",
		sequenceName = "match_streak_seq",
		allocationSize = 50
	)
	private Long id;

	@Column(name = "date", nullable = false)
	private LocalDate date; // yyyy-MM-dd

	@Column(name = "game_count", nullable = false)
	private int allGameCount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "search_history_id")
	@Setter
	private SearchHistory searchHistory;

	@Override
	public int compareTo(MatchStreak st) {
		return this.date.compareTo(st.date);
	}

}

