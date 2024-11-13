package mejai.mejaigg.watch.dto.watch;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(description = "하루 플레이 기록")
@AllArgsConstructor
@Data
public class DayLogDto {
	@Schema(description = "하루 총 플레이 횟수", example = "1")
	private int playCount;

	@Schema(description = "하루 총 플레이 시간", example = "ss")
	private long playTime;
}
