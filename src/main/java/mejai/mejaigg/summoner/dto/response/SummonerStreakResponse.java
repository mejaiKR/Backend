package mejai.mejaigg.summoner.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "소환사 게임 횟수 및 승패 조회 응답 모델")
public class SummonerStreakResponse {
	private List<SummonerStreak> streak;
	private LocalDateTime lastUpdateAt;

	public SummonerStreakResponse(List<SummonerStreak> streak, LocalDateTime lastUpdateAt) {
		this.streak = streak;
		this.lastUpdateAt = lastUpdateAt;
	}
}
