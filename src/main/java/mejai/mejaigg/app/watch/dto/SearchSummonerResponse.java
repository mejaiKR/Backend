package mejai.mejaigg.app.watch.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import mejai.mejaigg.rank.domain.Rank;
import mejai.mejaigg.summoner.domain.Summoner;

@Data
public class SearchSummonerResponse {
	private String summonerName;

	private String tag;

	private String profileIcon;

	private String soloRankTier;

	private String soloRankIconUrl;

	@Schema(description = "자유랭크 티어", example = "")
	private String flexRankTier;

	private String flexRankIconUrl;

	public SearchSummonerResponse(Summoner summoner, String resourceUrl) {
		Rank soloRank = summoner.getSoloRank();
		Rank flexRank = summoner.getFlexRank();

		this.summonerName = summoner.getSummonerName();
		this.tag = summoner.getTagLine();
		this.profileIcon = resourceUrl + "profileIcon/" + summoner.getProfileIconId() + ".png";
		this.soloRankTier = soloRank.getTier() + " " + soloRank.getRank();
		this.soloRankIconUrl = soloRank.getIconUrl(resourceUrl);
		this.flexRankTier = flexRank.getTier() + " " + flexRank.getRank();
		this.flexRankIconUrl = flexRank.getIconUrl(resourceUrl);
	}
}
