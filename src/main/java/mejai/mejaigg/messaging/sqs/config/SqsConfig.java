package mejai.mejaigg.messaging.sqs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Configuration
@RequiredArgsConstructor
public class SqsConfig {
	private final AwsProperties awsProperties;

	@Bean
	public SqsAsyncClient sqsAsyncClient() {
		return SqsAsyncClient.builder()
			.region(Region.AP_NORTHEAST_2) // 리전 설정
			.credentialsProvider(StaticCredentialsProvider.create(
				AwsBasicCredentials.create(awsProperties.getAccessKey(), awsProperties.getSecretKey()))) // AWS 자격 증명 설정
			.build();
	}
}
