package mejai.mejaigg.watch.dto.watch;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Schema(description = "소환사 감시 등록 응답 모델")
@AllArgsConstructor
public class GetWatchSummonerDto {
	SummonerDto summoner;
	DayLogDto today;
	List<PlayLogDto> todayPlayLogs;
	List<DayLogDto> thisWeek;
}
