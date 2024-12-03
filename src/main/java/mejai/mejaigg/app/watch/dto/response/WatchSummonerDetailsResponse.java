package mejai.mejaigg.app.watch.dto.response;

import java.util.List;

import lombok.Data;
import mejai.mejaigg.app.watch.dto.response.watch.PlayLog;

@Data
public class WatchSummonerDetailsResponse {
	List<List<PlayLog>> playLogs;

	public WatchSummonerDetailsResponse(List<List<PlayLog>> playLogs) {
		this.playLogs = playLogs;
	}
}
