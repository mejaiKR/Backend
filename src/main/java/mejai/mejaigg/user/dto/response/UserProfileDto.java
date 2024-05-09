package mejai.mejaigg.user.dto.response;

import java.util.HashSet;
import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import mejai.mejaigg.rank.entity.Rank;
import mejai.mejaigg.user.entity.User;

@Getter
@Setter
@Schema(description = "소환사 프로필 조회 응답 모델")
public class UserProfileDto {
	@Schema(description = "소환사 이름", example = "hide on bush")
	private String userName;

	@Schema(description = "소환사 태그라인", example = "KR1")
	private String tagLine;

	@Schema(description = "소환사 프로필 이미지 URL", example = "http://localhost:8080/profileIcon/6.png")
	private String profileIcon;

	@Schema(description = "소환사 레벨", example = "745")
	private Long level;

	@Schema(description = "소환사 랭크 정보", example = "[{'tier':'CHALLENGER','rank':'I','leaguePoints':704,'wins':220,'losses':184,'tierIcon':'http://localhost:8080/emblem/Challenger.png'}]")
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
