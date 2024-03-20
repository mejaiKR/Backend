package mejai.mejaigg.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import mejai.mejaigg.common.RomanNumber;
import mejai.mejaigg.dto.riot.RankDto;

@Entity
@Getter
public class Rank {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // ID 자동 생성 전략 사용
	@Column(name = "rank_id")
	private Long id; // Rank의 고유 ID

	@ManyToOne(fetch = FetchType.LAZY)
	private User user; // Rank를 가지고 있는 User

	private String tier; //ex EMERALD
	private int rank; //ex IV :  String ->INT
	private String tierImage; //해당 티어 이미지를 url로 저장 추후 정규화 필요할지도.
	private Long leaguePoints;
	private String leagueId;
	private int wins;
	private int losses;
	private boolean hotStreak; //연승 여부
	private boolean veteran; //베테랑 여부
	private boolean freshBlood; //신규 여부
	private boolean inactive; //휴식 여부

	private int season;//시즌 몇 인지

	public void setUser(User user) {
		this.user = user;
	}

	public void setRankByRankDto(RankDto rankDto, int season) {
		this.tier = rankDto.getTier();
		this.rank = RomanNumber.romanToInt(rankDto.getRank());
		this.leaguePoints = rankDto.getLeaguePoints();
		this.wins = rankDto.getWins();
		this.losses = rankDto.getLosses();
		this.hotStreak = rankDto.isHotStreak();
		this.veteran = rankDto.isVeteran();
		this.freshBlood = rankDto.isFreshBlood();
		this.inactive = rankDto.isInactive();
		this.leagueId = rankDto.getLeagueId();
		this.season = season;

	}
}
