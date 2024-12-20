package mejai.mejaigg.searchhistory.dto;

import org.springdoc.core.annotations.ParameterObject;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
@ParameterObject
public class RankingRequestDto {
	@Schema(description = "조회할 년도", example = "2021")
	@Min(2021)
	private int year;

	@Schema(description = "조회할 월", example = "8")
	@Min(1)
	@Max(12)
	private int month;
}
