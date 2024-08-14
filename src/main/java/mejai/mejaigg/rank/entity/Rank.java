package mejai.mejaigg.rank.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mejai.mejaigg.global.jpa.BaseEntity;
import mejai.mejaigg.rank.dto.RankDto;
import mejai.mejaigg.summoner.entity.User;

@Getter
@Entity
@Builder
@ToString
@IdClass(RankId.class)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Rank extends BaseEntity {
	@Id
	@Setter
	@Column(name = "rank_puuid")
	private String id;

	@Id
	@Column(name = "queue_type")
	private String queueType; //RANKED_SOLO_5x5

	@ManyToOne
	@MapsId
	@Setter
	@JoinColumn(name = "user_id")
	private User user;

	@Column(name = "tier")
	private String tier; //ex EMERALD

	@Column(name = "rank")
	private String rank; //ex IV :  String ->INT

	@Column(name = "league_points")
	private Long leaguePoints;

	@Column(name = "league_id")
	private String leagueId;

	@Column(name = "wins")
	private int wins;

	@Column(name = "losses")
	private int losses;

	@Column(name = "hot_streak")
	private boolean hotStreak; //연승 여부

	@Column(name = "veteran")
	private boolean veteran; //베테랑 여부

	@Column(name = "fresh_blood")
	private boolean freshBlood; //신규 여부

	@Column(name = "inactive")
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


