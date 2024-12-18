package mejai.mejaigg.app.jwt.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "apple")
public class AppleProperties extends SocialProperties {
}
