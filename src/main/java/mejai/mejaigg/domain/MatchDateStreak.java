package mejai.mejaigg.domain;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class MatchDateStreak {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // ID 자동 생성 전략 사용
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	private Date date; // yyyy-MM-dd

	private int allGameCount;
	private int rankGameCount;

	@OneToMany(mappedBy = "matchDateStreak")
	private Set<Match> matchIds = new HashSet<>();
}
