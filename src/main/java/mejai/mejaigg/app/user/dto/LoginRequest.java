package mejai.mejaigg.app.user.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import mejai.mejaigg.app.user.domain.SocialType;

@Data
@Getter
public class LoginRequest {
	@Schema(description = "소셜 ID", example = "1")
	private String socialId;

	@Schema(description = "소셜 타입", example = "kakao")
	private SocialType socialType;

	@JsonCreator
	public LoginRequest(String socialId, String socialType) {
		this.socialId = socialId;
		this.socialType = SocialType.of(socialType);
	}
}
