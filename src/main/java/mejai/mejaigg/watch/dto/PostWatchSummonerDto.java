package mejai.mejaigg.watch.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "소환사 감시 등록 응답 모델")
public class PostWatchSummonerDto {
	@Schema(description = "소환사 ID", example = "1")
	private long summonerId;

	public PostWatchSummonerDto(long summonerId) {
		this.summonerId = summonerId;
	}
}
