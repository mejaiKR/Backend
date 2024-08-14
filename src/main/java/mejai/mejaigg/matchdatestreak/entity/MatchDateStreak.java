package mejai.mejaigg.matchdatestreak.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mejai.mejaigg.global.jpa.BaseEntity;
import mejai.mejaigg.searchhistory.entity.SearchHistory;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchDateStreak extends BaseEntity implements Comparable<MatchDateStreak> {

	@Id
	@GeneratedValue // ID 자동 생성 전략 사용
	private Long id;

	@Column(name = "date", nullable = false)
	private LocalDate date; // yyyy-MM-dd

	@Column(name = "game_count", nullable = false)
	private int allGameCount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "history_id")
	@Setter
	private SearchHistory searchHistory;

	@Override
	public int compareTo(MatchDateStreak st) {
		return this.date.compareTo(st.date);
	}

}
