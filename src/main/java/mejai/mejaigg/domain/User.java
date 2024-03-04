package mejai.mejaigg.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;

/**
 * user
 * /riot/account/v1/accounts/by-riot-id/{gameName}/{tagLine}
 get puuid in response

 /lol/summoner/v4/summoners/by-puuid/{encryptedPUUID}

 */
@Entity
@Getter
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

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Set<Rank> ranks = new HashSet<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Set<MatchParticipant> matchParticipants = new HashSet<>();

}
