package mejai.mejaigg.app.user.dto;

import lombok.Data;

@Data
public class RefreshResponse {
	private final String accessToken;
	private final String refreshToken;

	public RefreshResponse(String accessToken, String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}
}
