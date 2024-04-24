package mejai.mejaigg.user.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import mejai.mejaigg.common.jpa.BaseEntity;
import mejai.mejaigg.rank.entity.Rank;
import mejai.mejaigg.riot.dto.SummonerDto;
import mejai.mejaigg.searchHistory.entity.SearchHistory;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User extends BaseEntity {
	@Id
	private String puuid;

	private String tagLine;

	//Encrypted account ID, Max length 56 characters.
	private String accountId;

	private String summonerId;
	private String summonerName;

	//Date summoner was last modified specified as epoch milliseconds.
	private Long revisionDate;
	private int profileIconId;
	private Long summonerLevel;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Rank> rank = new HashSet<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
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
