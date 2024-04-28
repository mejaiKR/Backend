package mejai.mejaigg.user.dto.response;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import mejai.mejaigg.rank.entity.Rank;
import mejai.mejaigg.user.entity.User;

@Getter
@Setter
public class UserProfileDto {
	private String userName;
	private String tagLine;
	private String profileIcon;
	private Long level;
	private Set<RankResponseDto> rank = new HashSet<>();

	public void setByUser(User user, String resourceUrl) {
		Set<Rank> ranks = user.getRank();
		Rank soloRank = ranks.stream().filter(rank -> rank.getQueueType().equals("RANKED_SOLO_5x5")).findFirst()
			.orElse(null);
		Rank flexRank = ranks.stream().filter(rank -> rank.getQueueType().equals("RANKED_FLEX_SR")).findFirst()
			.orElse(null);
		RankResponseDto responseRank = new RankResponseDto();
		responseRank.setByRank(soloRank, resourceUrl);
		this.rank.add(responseRank);
		responseRank = new RankResponseDto();
		responseRank.setByRank(flexRank, resourceUrl);
		this.rank.add(responseRank);
		this.userName = user.getSummonerName();
		this.profileIcon = resourceUrl + "profileIcon/" + user.getProfileIconId() + ".png";
		this.level = user.getSummonerLevel();
		this.tagLine = user.getTagLine();
	}

	public void setDummy() {
		this.userName = "hide on bush";
		this.profileIcon = "http://localhost:8080/profileIcon/6.png";
		this.level = 745L;
		this.tagLine = "KR1";
	}
}
