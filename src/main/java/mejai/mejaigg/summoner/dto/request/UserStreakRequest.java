package mejai.mejaigg.summoner.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import mejai.mejaigg.global.validation.FutureDate;

@Data
@FutureDate
@Schema(description = "소환사 게임 횟수 및 승패 조회 요청")
@Builder
public class UserStreakRequest {

	@Schema(description = "소환사 아이디", example = "hide on bush")
	@NotBlank
	@Size(min = 1, max = 30)
	private String id;

	@Schema(description = "소환사 태그", example = "Kr1")
	@NotBlank
	@Size(min = 2, max = 10)
	@Builder.Default
	private String tag = "Kr1";

	@Schema(description = "조회할 년도", example = "2021")
	@Min(2021)
	private int year;

	@Schema(description = "조회할 월", example = "8")
	@Min(1)
	@Max(12)
	private int month;

}
