package mejai.mejaigg.summoner.dto.response;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import mejai.mejaigg.matchdatestreak.domain.MatchStreak;

@Data
@Schema(description = "소환사 게임 횟수 및 승패 조회 응답 모델")
public class UserStreakDto {
	@Schema(description = "게임한 날짜", example = "2023-12-01")
	private LocalDate date;

	@Schema(description = "해당 날짜에 진행된 총 게임 횟수", example = "10")
	private int gameCount;

	@Schema(description = "해당 날짜에 진행된 판수에 맞는 메자이 이미지 URL", example = "http://localhost:8080/mejaiStack/1.svg")
	private String imageUrl;
	// private int rankCount;

	public void setByMatchDateStreak(MatchStreak matchStreak, String resourceUrl) {
		this.date = matchStreak.getDate();
		this.gameCount = matchStreak.getAllGameCount();
		if (gameCount > 25) {
			this.imageUrl = resourceUrl + "deathCap.png";
		} else {
			this.imageUrl = resourceUrl + "mejaiStack/" + gameCount + ".svg";
		}
	}
}
