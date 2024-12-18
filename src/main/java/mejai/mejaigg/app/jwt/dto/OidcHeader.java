package mejai.mejaigg.app.jwt.dto;

import lombok.Data;

@Data
public class OidcHeader {
	private String kid;
	private String typ;
	private String alg;
}
