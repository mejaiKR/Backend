package mejai.mejaigg.riot.service;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "riot")
public class RiotProperties {
	private String apiKey;
}
