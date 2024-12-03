package mejai.mejaigg.app.watch.dto.request;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class GetWatchSummonerRequest {
	@Schema(description = "시작일", example = "2024-01-01", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
	private LocalDate startDate;

	public GetWatchSummonerRequest(LocalDate startDate) {
		this.startDate = startDate;
	}

	public GetWatchSummonerRequest() {
		this(LocalDate.now());
	}
}
