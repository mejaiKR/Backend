package mejai.mejaigg.app.jwt.client;

import mejai.mejaigg.app.jwt.dto.JwksResponse;

public interface JwksClient {
	JwksResponse getJwks();
}
