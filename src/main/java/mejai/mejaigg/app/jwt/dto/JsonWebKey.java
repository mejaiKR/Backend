package mejai.mejaigg.app.jwt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class JsonWebKey {
	private String kid;
	private String kty;
	private String alg;
	private String use;

	@JsonProperty("n")
	private String modulus;

	@JsonProperty("e")
	private String exponent;
}
