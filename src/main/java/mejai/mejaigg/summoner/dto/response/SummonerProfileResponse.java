package mejai.mejaigg.summoner.dto.response;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

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
	private String tag;

	@Schema(description = "소환사 프로필 이미지 URL", example = "http://localhost:8080/profileIcon/6.png")
	private String profileIcon;

	@Schema(description = "소환사 레벨", example = "745")
	private Long level;

	@Schema(description = "소환사 랭크 정보", example = "[{'tier':'CHALLENGER','rank':'I','leaguePoints':704,'wins':220,'losses':184,'tierIcon':'http://localhost:8080/emblem/Challenger.png'}]")
	private List<RankResponseDto> rank = new LinkedList<>();

	private LocalDateTime lastUpdatedAt;

	public SummonerProfileResponse(Summoner summoner, String resourceUrl) {
		List<Rank> ranks = summoner.getRanks();
		Rank soloRank = ranks.stream().filter(rank -> rank.getId().getQueueType().equals("RANKED_SOLO_5x5")).findFirst()
			.orElseThrow(IllegalArgumentException::new);
		Rank flexRank = ranks.stream().filter(rank -> rank.getId().getQueueType().equals("RANKED_FLEX_SR")).findFirst()
			.orElseThrow((IllegalArgumentException::new));
		RankResponseDto responseRank = new RankResponseDto();
		responseRank.setByRank(soloRank, resourceUrl);
		this.rank.add(responseRank);
		responseRank = new RankResponseDto();
		responseRank.setByRank(flexRank, resourceUrl);
		this.id = summoner.getId();
		this.rank.add(responseRank);
		this.summonerName = summoner.getSummonerName();
		this.profileIcon = resourceUrl + "profileIcon/" + summoner.getProfileIconId() + ".png";
		this.level = summoner.getSummonerLevel();
		this.tag = summoner.getTagLine();
		this.lastUpdatedAt = summoner.getUpdatedAt();
	}

	public void setDummy() {
		this.summonerName = "hide on bush";
		this.profileIcon = "http://localhost:8080/profileIcon/6.png";
		this.level = 745L;
		this.tag = "KR1";
	}
}
