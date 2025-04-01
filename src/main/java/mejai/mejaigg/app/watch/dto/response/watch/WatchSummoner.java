package mejai.mejaigg.app.watch.dto.response.watch;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import mejai.mejaigg.app.user.domain.AppUser;
import mejai.mejaigg.rank.domain.Rank;
import mejai.mejaigg.summoner.domain.Summoner;

@Data
public class WatchSummoner {
	private String summonerName;

	private String tag;

	private String profileIcon;

	@Schema(description = "관계", example = "애인, 가족, 친구")
	private String relationship;

	private String soloRankTier;

	private String soloRankIconUrl;

	@Schema(description = "자유랭크 티어", example = "")
	private String flexRankTier;

	private String flexRankIconUrl;

	private LocalDateTime lastUpdatedWatchSummoner;

	public WatchSummoner(AppUser user, String resourceUrl) {
		Summoner summoner = user.getWatchSummoner();
		Rank soloRank = summoner.getSoloRank();
		Rank flexRank = summoner.getFlexRank();

		this.summonerName = summoner.getSummonerName();
		this.tag = summoner.getTagLine();
		this.profileIcon = resourceUrl + "profileIcon/" + summoner.getProfileIconId() + ".png";
		this.relationship = user.getRelationship();
		this.soloRankTier = soloRank.getTier() + " " + soloRank.getRank();
		this.soloRankIconUrl = soloRank.getIconUrl(resourceUrl);
		this.flexRankTier = flexRank.getTier() + " " + flexRank.getRank();
		this.flexRankIconUrl = flexRank.getIconUrl(resourceUrl);
		this.lastUpdatedWatchSummoner = user.getLastUpdatedWatchSummoner();
	}
}
