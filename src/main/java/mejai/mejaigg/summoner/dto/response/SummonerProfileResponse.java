package mejai.mejaigg.summoner.dto.response;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import mejai.mejaigg.rank.domain.Rank;
import mejai.mejaigg.summoner.domain.Summoner;

@Schema(description = "소환사 프로필 조회 응답 모델")
@NoArgsConstructor
@Data
public class SummonerProfileResponse {
	@Schema(description = "소환사 DB Id", example = "1")
	private Long id;

	@Schema(description = "소환사 이름", example = "hide on bush")
	private String summonerName;

	@Schema(description = "소환사 태그라인", example = "KR1")
	private String tagLine;

	@Schema(description = "소환사 프로필 이미지 URL", example = "http://localhost:8080/profileIcon/6.png")
	private String profileIcon;

	@Schema(description = "소환사 레벨", example = "745")
	private Long level;

	@Schema(description = "소환사 개인 랭크")
	private RankResponse soloRank;

	@Schema(description = "소환사 자유 랭크")
	private RankResponse flexRank;

	private LocalDateTime lastUpdatedAt;

	public SummonerProfileResponse(Summoner summoner, String resourceUrl) {
		Rank soloRank = summoner.getSoloRank();
		Rank flexRank = summoner.getFlexRank();

		this.id = summoner.getId();
		this.summonerName = summoner.getSummonerName();
		this.profileIcon = resourceUrl + "profileIcon/" + summoner.getProfileIconId() + ".png";
		this.level = summoner.getSummonerLevel();
		this.tagLine = summoner.getTagLine();
		this.soloRank = new RankResponse(soloRank, resourceUrl);
		this.flexRank = new RankResponse(flexRank, resourceUrl);
		this.lastUpdatedAt = summoner.getUpdatedAt();
	}

	public void setDummy() {
		this.summonerName = "hide on bush";
		this.profileIcon = "http://localhost:8080/profileIcon/6.png";
		this.level = 745L;
		this.tagLine = "KR1";
	}
}
