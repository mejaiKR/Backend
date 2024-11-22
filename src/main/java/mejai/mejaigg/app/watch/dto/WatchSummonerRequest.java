package mejai.mejaigg.app.watch.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import mejai.mejaigg.app.user.domain.Relationship;

@Data
@Schema(description = "소환사 식별자")
public class WatchSummonerRequest {
	@NotBlank
	@Size(min = 1, max = 30)
	@Schema(description = "소환사명", example = "hide on bush")
	private String summonerName;

	@NotBlank
	@Size(min = 2, max = 10)
	@Schema(description = "소환사 태그", example = "KR1")
	private String tag = "KR1";

	@Schema(description = "관계", example = "애인")
	private Relationship relationship;
}
