package mejai.mejaigg.app.user.dto;

import lombok.Data;

@Data
public class RefreshResponse {
	private final String accessToken;

	public RefreshResponse(String accessToken) {
		this.accessToken = accessToken;
	}
}
