package mejai.mejaigg.app.watch.dto.watch;

import java.time.LocalTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PlayLog {
	@Schema(description = "게임 시작 시간", example = "hh:mm:ss.nnn")
	private LocalTime startTime;

	@Schema(description = "게임 끝난 시간", example = "hh:mm:ss:nnn")
	private LocalTime endTime;

	@Schema(description = "게임 승리 여부, true = 승리", example = "true")
	private boolean win;
}
