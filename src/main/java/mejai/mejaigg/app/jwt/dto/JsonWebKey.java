package mejai.mejaigg.app.jwt.dto;

import lombok.Data;

@Data
public class JsonWebKey {
	private String kid;
	private String kty;
	private String alg;
	private String use;
	private String n;
	private String e;
}
