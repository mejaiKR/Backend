package mejai.mejaigg.app.jwt.dto;

import java.util.List;

import lombok.Data;

@Data
public class JwksResponse {
	private List<JsonWebKey> keys;
}
