package mejai.mejaigg.matchdatestreak.entity;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import mejai.mejaigg.global.jpa.BaseEntity;
import mejai.mejaigg.searchhistory.entity.SearchHistory;

@Entity
@Getter
@Setter
public class MatchDateStreak extends BaseEntity implements Comparable<MatchDateStreak> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // ID 자동 생성 전략 사용
	private Long id;

	private Date date; // yyyy-MM-dd

	private int allGameCount;

	@ManyToOne(fetch = FetchType.LAZY)
	private SearchHistory searchHistory;

	@Override
	public int compareTo(MatchDateStreak st) {
		return this.date.compareTo(st.date);
	}

}
