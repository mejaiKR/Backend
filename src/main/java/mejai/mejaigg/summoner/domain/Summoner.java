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
	private List<Rank> rank = new LinkedList<>();

	@OneToMany(mappedBy = "summoner", cascade = CascadeType.ALL)
	@Builder.Default
	private Set<SearchHistory> searchHistory = new HashSet<>();

	public void updateBySummonerDto(SummonerDto summonerDto) {
		this.summonerId = summonerDto.getId();
		this.revisionDate = summonerDto.getRevisionDate();
		this.profileIconId = summonerDto.getProfileIconId();
		this.summonerLevel = summonerDto.getSummonerLevel();
	}

	public void setRank(List<Rank> ranks) {
		this.rank = ranks;
		ranks.forEach(rank -> rank.setSummoner(this));
	}

	public void addSearchHistory(SearchHistory searchHistory) {
		this.searchHistory.add(searchHistory);
	}
}
