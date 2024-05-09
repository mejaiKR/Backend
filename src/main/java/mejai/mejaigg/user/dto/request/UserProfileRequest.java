package mejai.mejaigg.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "소환사 정보 조회 요청")
public class UserProfileRequest {

	@NotBlank
	@Size(min = 1, max = 30)
	@Schema(description = "소환사 아이디", example = "hide on bush")
	private String id;

	@NotBlank
	@Size(min = 2, max = 10)
	@Schema(description = "소환사 태그", example = "Kr1")
	private String tag = "Kr1";

}
