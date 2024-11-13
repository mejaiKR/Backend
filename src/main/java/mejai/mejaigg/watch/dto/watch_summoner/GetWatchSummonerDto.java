package mejai.mejaigg.watch.dto.watch_summoner;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "소환사 감시 등록 응답 모델")
@AllArgsConstructor
public class GetWatchSummonerDto {
	SummonerDto summoner;
	DayLogDto today;
	List<PlayLogDto> todayPlayLogs;
	List<DayLogDto> thisWeek;
}
