package mejai.mejaigg.riot.service;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@ConfigurationProperties(prefix = "riot.api")
@AllArgsConstructor
public class RiotProperties {
	private String apiKey;
}
