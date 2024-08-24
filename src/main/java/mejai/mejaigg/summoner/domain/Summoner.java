package mejai.mejaigg.summoner.domain;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mejai.mejaigg.global.jpa.BaseEntity;
import mejai.mejaigg.rank.domain.Rank;
import mejai.mejaigg.rank.domain.RankId;
import mejai.mejaigg.rank.dto.RankDto;
import mejai.mejaigg.riot.dto.SummonerDto;
import mejai.mejaigg.searchhistory.domain.SearchHistory;

@Entity
@Getter
@Table(name = "summoner")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Summoner extends BaseEntity {
	@Id
	@Column(name = "id")
	@GeneratedValue // ID 자동 생성 전략 사용
	private Long id;

	@Column(name = "summoner_name", nullable = false)
	private String summonerName;

	@Column(name = "tag_line")
	private String tagLine;

	@Column(name = "puuid", nullable = false)
	private String puuid;

	//Encrypted account ID, Max length 56 characters.
	@Column(name = "account_id", length = 70, nullable = false)
	private String accountId;

	@Column(name = "summoner_id", nullable = false)
	private String summonerId;

	//Date summoner was last modified specified as epoch milliseconds.
	@Column(name = "revision_date", nullable = false)
	private Long revisionDate;

	@Column(name = "profile_icon_id", nullable = false)
	private int profileIconId;

	@Column(name = "summoner_level", nullable = false)
	private Long summonerLevel;

	@OneToMany(mappedBy = "summoner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@Builder.Default
	private List<Rank> ranks = new LinkedList<>();

	@OneToMany(mappedBy = "summoner", cascade = CascadeType.ALL)
	@Builder.Default
	private Set<SearchHistory> searchHistory = new HashSet<>();

	public void updateBySummonerDto(SummonerDto summonerDto) {
		this.summonerId = summonerDto.getId();
		this.revisionDate = summonerDto.getRevisionDate();
		this.profileIconId = summonerDto.getProfileIconId();
		this.summonerLevel = summonerDto.getSummonerLevel();
	}

	public void setRanks(List<Rank> ranks) {
		this.ranks = ranks;
		ranks.forEach(rank -> rank.setSummoner(this));
	}

	public void setRankByRankDtos(Set<RankDto> rankDtos) {
		List<Rank> ranks = new LinkedList<>();
		rankDtos.forEach(rankDto -> {
			if (rankDto.getQueueType().equals("RANKED_SOLO_5x5") || rankDto.getQueueType().equals("RANKED_FLEX_SR")) {
				Rank rank = Rank.builder()
					.id(new RankId(this.getId(), rankDto.getQueueType()))
					.tier(rankDto.getTier())
					.rank(rankDto.getRank())
					.leaguePoints(rankDto.getLeaguePoints())
					.wins(rankDto.getWins())
					.losses(rankDto.getLosses())
					.veteran(rankDto.isVeteran())
					.inactive(rankDto.isInactive())
					.freshBlood(rankDto.isFreshBlood())
					.hotStreak(rankDto.isHotStreak())
					.summoner(this)
					.build();
				ranks.add(rank);
			}
		});
		// 랭크 정보가 없는 경우 unRanked로 추가
		if (rankDtos.stream().noneMatch(rank -> rank.getQueueType().equals("RANKED_SOLO_5x5")))
			ranks.add(Rank.builder()
				.id(new RankId(this.getId(), "RANKED_SOLO_5x5"))
				.summoner(this)
				.build());
		if (rankDtos.stream().noneMatch(rank -> rank.getQueueType().equals("RANKED_FLEX_SR")))
			ranks.add(Rank.builder()
				.id(new RankId(this.getId(), "RANKED_FLEX_SR"))
				.summoner(this)
				.build());
		this.ranks = ranks;
	}

	public void addSearchHistory(SearchHistory searchHistory) {
		this.searchHistory.add(searchHistory);
	}
}
