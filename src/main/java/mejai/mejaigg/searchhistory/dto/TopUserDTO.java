package mejai.mejaigg.searchhistory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TopUserDTO {
	@Schema(description = "유저이름", example = "hide on bush")
	private String summonerName;
	@Schema(description = "tag 이름", example = "KR1")
	private String tagLine;
	@Schema(description = "tag 이름", example = "KR1")
	private long totalGameCount;
}
