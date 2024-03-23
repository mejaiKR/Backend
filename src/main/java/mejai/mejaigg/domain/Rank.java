package mejai.mejaigg.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class Rank {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // ID 자동 생성 전략 사용
	@Column(name = "rank_id")
	private Long id; // Rank의 고유 ID

	@OneToOne(fetch = FetchType.LAZY)
	private User user; // Rank를 가지고 있는 User

	private String tier; //ex EMERALD
	private String rank; //ex IV :  String ->INT
	private String tierImage; //해당 티어 이미지를 url로 저장 추후 정규화 필요할지도.
	private Long leaguePoints;
	private String leagueId;
	private int wins;
	private int losses;
	private boolean hotStreak; //연승 여부
	private boolean veteran; //베테랑 여부
	private boolean freshBlood; //신규 여부
	private boolean inactive; //휴식 여부
	private String queueType; //RANKED_SOLO_5x5
	private int season;//시즌 몇 인지

	public void setUser(User user) {
		this.user = user;
	}

	public void setUnRanked() {
		this.tier = "UNRANKED";
		this.rank = "I";
		this.leaguePoints = 0L;
		this.wins = 0;
		this.losses = 0;
		this.hotStreak = false;
		this.veteran = false;
		this.freshBlood = false;
		this.inactive = false;
	}

}
