package mejai.mejaigg.app.jwt.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
	private String secret;
	private long accessTokenExpiration;
	private long refreshTokenExpiration;
}
