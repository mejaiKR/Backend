package mejai.mejaigg.app.jwt.config;

import lombok.Data;

@Data
public abstract class SocialProperties {
	private String appKey;
	private String issuer;
}
