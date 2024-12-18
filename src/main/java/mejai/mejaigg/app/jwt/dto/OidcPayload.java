package mejai.mejaigg.app.jwt.dto;

import lombok.Data;

@Data
public class OidcPayload {
	private String iss;
	private String aud;
	private String sub;
	private Long iat;
	private Long exp;
	private Long authTime;

	@Override
	public String toString() {
		return "OidcPayload [iss=" + iss + ", aud=" + aud + ", sub=" + sub + ", iat=" + iat + ", exp=" + exp
			+ ", authTime=" + authTime + "]";
	}

	public void validate(String appKey, String issuer) {
		Long now = System.currentTimeMillis() / 1000;
		if (exp < now) {
			throw new IllegalArgumentException("만료된 토큰입니다.");
		}
		if (!iss.equals(issuer)) {
			throw new IllegalArgumentException("잘못된 issuer 입니다.");
		}
		if (!aud.equals(appKey)) {
			throw new IllegalArgumentException("잘못된 audience 입니다.");
		}
	}
}
