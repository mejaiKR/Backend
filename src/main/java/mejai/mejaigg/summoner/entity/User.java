package mejai.mejaigg.summoner.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mejai.mejaigg.global.jpa.BaseEntity;
import mejai.mejaigg.rank.entity.Rank;
import mejai.mejaigg.riot.dto.SummonerDto;
import mejai.mejaigg.searchhistory.entity.SearchHistory;

@Entity
@Getter
@Table(name = "users")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
	@Id
	@Column(name = "user_puuid")
	private String id;

	@Column(name = "tag_line")
	private String tagLine;

	//Encrypted account ID, Max length 56 characters.
	@Column(name = "account_id", length = 70, nullable = false)
	private String accountId;

	@Column(name = "summoner_id", nullable = false)
	private String summonerId;

	@Column(name = "summoner_name", nullable = false)
	private String summonerName;

	//Date summoner was last modified specified as epoch milliseconds.
	@Column(name = "revision_date", nullable = false)
	private Long revisionDate;

	@Column(name = "profile_icon_id", nullable = false)
	private int profileIconId;

	@Column(name = "summoner_level", nullable = false)
	private Long summonerLevel;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@Setter
	@Builder.Default
	private Set<Rank> rank = new HashSet<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	@Builder.Default
	private Set<SearchHistory> searchHistory = new HashSet<>();

	public void updateBySummonerDto(SummonerDto summonerDto) {
		this.summonerId = summonerDto.getId();
		this.revisionDate = summonerDto.getRevisionDate();
		this.profileIconId = summonerDto.getProfileIconId();
		this.summonerLevel = summonerDto.getSummonerLevel();
	}

	public void addSearchHistory(SearchHistory searchHistory) {
		this.searchHistory.add(searchHistory);
	}
}
