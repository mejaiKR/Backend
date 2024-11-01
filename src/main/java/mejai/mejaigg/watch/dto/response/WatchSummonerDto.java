package mejai.mejaigg.watch.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "소환사 감시 등록 응답 모델")
public class WatchSummonerDto {
	@Schema(description = "소환사 ID", example = "1")
	private long summonerId;

	public WatchSummonerDto(long summonerId) {
		this.summonerId = summonerId;
	}
}
