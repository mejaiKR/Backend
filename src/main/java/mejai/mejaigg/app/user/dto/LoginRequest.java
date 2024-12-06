package mejai.mejaigg.app.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import mejai.mejaigg.app.user.domain.SocialType;

@Data
public class LoginRequest {
	@Schema(description = "소셜 ID", example = "1")
	private String socialId;

	@Schema(description = "소셜 타입", example = "kakao")
	private SocialType socialType;

	private String authCode;
}
