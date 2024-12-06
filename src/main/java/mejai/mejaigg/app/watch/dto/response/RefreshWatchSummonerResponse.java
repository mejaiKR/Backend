package mejai.mejaigg.app.watch.dto.response;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "소환사 감시 등록 응답 모델")
public class RefreshWatchSummonerResponse {
	@Schema(description = "마지막 갱신 시간", example = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime lastUpdatedWatchSummoner;

	public RefreshWatchSummonerResponse(LocalDateTime lastUpdatedWatchSummoner) {
		this.lastUpdatedWatchSummoner = lastUpdatedWatchSummoner;
	}
}
