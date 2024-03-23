package mejai.mejaigg.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * user
 * /riot/account/v1/accounts/by-riot-id/{gameName}/{tagLine}
 get puuid in response

 /lol/summoner/v4/summoners/by-puuid/{encryptedPUUID}

 */
@Entity
@Data
@Table(name = "users")
public class User {
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

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private Rank rank;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Set<MatchParticipant> matchParticipants = new HashSet<>();

	public void addMatchParticipant(MatchParticipant matchParticipant) {
		matchParticipants.add(matchParticipant);
		matchParticipant.setUser(this);
	}

	public void setRank(Rank rank) {
		this.rank = rank;
		rank.setUser(this);
	}
}
