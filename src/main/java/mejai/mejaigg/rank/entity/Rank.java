package mejai.mejaigg.rank.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mejai.mejaigg.global.jpa.BaseEntity;
import mejai.mejaigg.rank.dto.RankDto;
import mejai.mejaigg.summoner.entity.User;

@Entity
@IdClass(RankId.class)
@Getter
@Setter
@ToString
public class Rank extends BaseEntity {
	@Id
	private String puuid;

	@ManyToOne
	@MapsId
	@JoinColumn(name = "puuid")
	private User user;

	@Id
	private String queueType; //RANKED_SOLO_5x5

	private String tier; //ex EMERALD
	private String rank; //ex IV :  String ->INT
	private Long leaguePoints;
	private String leagueId;
	private int wins;
	private int losses;
	private boolean hotStreak; //연승 여부
	private boolean veteran; //베테랑 여부
	private boolean freshBlood; //신규 여부
	private boolean inactive; //휴식 여부

	public void setUnRanked(boolean isSolo) {
		this.tier = "UNRANKED";
		this.rank = "I";
		this.leaguePoints = 0L;
		this.wins = 0;
		this.losses = 0;
		this.hotStreak = false;
		this.veteran = false;
		this.freshBlood = false;
		this.inactive = false;
		if (isSolo) {
			this.queueType = "RANKED_SOLO_5x5";
		} else {
			this.queueType = "RANKED_FLEX_SR";
		}
	}

	public void updateByRankDto(RankDto rankDto) {
		this.tier = rankDto.getTier();
		this.rank = rankDto.getRank();
		this.leaguePoints = rankDto.getLeaguePoints();
		this.leagueId = rankDto.getLeagueId();
		this.wins = rankDto.getWins();
		this.losses = rankDto.getLosses();
		this.hotStreak = rankDto.isHotStreak();
		this.veteran = rankDto.isVeteran();
		this.freshBlood = rankDto.isFreshBlood();
		this.inactive = rankDto.isInactive();
		this.queueType = rankDto.getQueueType();
	}
}
