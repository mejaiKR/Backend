package mejai.mejaigg.summoner.dto.response;

import java.time.format.DateTimeFormatter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import mejai.mejaigg.match.matchdatestreak.entity.MatchDateStreak;

@Data
@Schema(description = "소환사 게임 횟수 및 승패 조회 응답 모델")
public class UserStreakDto {
	@Schema(description = "게임한 날짜", example = "2023-12-01")
	private String date;

	@Schema(description = "해당 날짜에 진행된 총 게임 횟수", example = "10")
	private int gameCount;

	@Schema(description = "해당 날짜에 진행된 판수에 맞는 메자이 이미지 URL", example = "http://localhost:8080/mejaiStack/1.svg")
	private String imageUrl;
	// private int rankCount;

	public void setDummy(int year, int month, int day) {
		this.date = year + "-" + month + "-" + day;
		this.gameCount = 10;
		// this.rankCount = 5;
	}

	public void setByMatchDateStreak(MatchDateStreak matchDateStreak, String resourceUrl) {
		this.date = matchDateStreak.getDate().toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		this.gameCount = matchDateStreak.getAllGameCount();
		if (gameCount > 25) {
			this.imageUrl = resourceUrl + "deathCap.png";
		} else {
			this.imageUrl = resourceUrl + "mejaiStack/" + gameCount + ".svg";
		}
	}
}
