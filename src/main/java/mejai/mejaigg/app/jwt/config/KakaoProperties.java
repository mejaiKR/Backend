package mejai.mejaigg.app.jwt.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "kakao")
public class KakaoProperties {
	private String appKey;
	private String issuer;
}
