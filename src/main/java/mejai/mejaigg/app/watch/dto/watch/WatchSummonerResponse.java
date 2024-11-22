package mejai.mejaigg.app.watch.dto.watch;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Schema(description = "소환사 감시 등록 응답 모델")
@AllArgsConstructor
public class WatchSummonerResponse {
	WatchSummoner summoner;
	DayLog today;
	List<PlayLog> todayPlayLogs;
	List<DayLog> thisWeek;
}
