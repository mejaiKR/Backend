package mejai.mejaigg.rank.domain;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mejai.mejaigg.global.jpa.BaseEntity;
import mejai.mejaigg.rank.dto.RankDto;
import mejai.mejaigg.summoner.domain.Summoner;

@Getter
@Builder
@Entity
@Table(name = "rank")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Rank extends BaseEntity {

	@EmbeddedId
	private RankId id;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("id")
	@Setter
	private Summoner summoner;

	@Column(name = "tier")
	@Enumerated(EnumType.STRING)
	@ColumnDefault("'UNRANKED'")
	@Builder.Default
	private TierType tier = TierType.UNRANKED; //ex EMERALD

	@Column(name = "rank")
	@Enumerated(EnumType.STRING)
	@ColumnDefault("'I'")
	private RankType rank = RankType.I; //ex IV :  String ->INT

	@Column(name = "league_points")
	@ColumnDefault("0")
	private Long leaguePoints = 0L;

	@Column(name = "league_id")
	private String leagueId;

	@Column(name = "wins")
	@ColumnDefault("0")
	private int wins;

	@Column(name = "losses")
	@ColumnDefault("0")
	private int losses;

	@Column(name = "hot_streak")
	@ColumnDefault("false")
	private boolean hotStreak; //연승 여부

	@Column(name = "veteran")
	@ColumnDefault("false")
	private boolean veteran; //베테랑 여부

	@Column(name = "fresh_blood")
	@ColumnDefault("false")
	private boolean freshBlood; //신규 여부

	@Column(name = "inactive")
	@ColumnDefault("false")
	private boolean inactive; //휴식 여부

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
	}
}


