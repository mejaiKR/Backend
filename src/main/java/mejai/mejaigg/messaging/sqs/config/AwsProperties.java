package mejai.mejaigg.messaging.sqs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "cloud.aws")
public class AwsProperties {
	private String accessKey;
	private String secretKey;
	private String region;
}
