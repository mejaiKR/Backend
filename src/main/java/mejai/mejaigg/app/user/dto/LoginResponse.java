package mejai.mejaigg.app.user.dto;

import lombok.Data;

@Data
public class LoginResponse {
	private final String accessToken;
	private final String refreshToken;

	public LoginResponse(String accessToken, String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}
}
