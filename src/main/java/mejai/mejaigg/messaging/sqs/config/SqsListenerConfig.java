package mejai.mejaigg.messaging.sqs.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import io.awspring.cloud.sqs.listener.SqsMessageListenerContainer;
import lombok.RequiredArgsConstructor;
import mejai.mejaigg.messaging.sqs.listener.MyMessageListener;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Configuration
@RequiredArgsConstructor
public class SqsListenerConfig {
	private final ObjectMapper objectMapper;

	@Bean
	public SqsMessageListenerContainer<Object> sqsMessageListenerContainer(SqsAsyncClient sqsAsyncClient) {
		SqsMessageListenerContainerFactory<Object> factory = SqsMessageListenerContainerFactory.builder()
			.sqsAsyncClient(sqsAsyncClient)
			.configure(options -> options
				.maxConcurrentMessages(10)
				.pollTimeout(Duration.ofSeconds(10)))
			.build();
		// SqsMessageListenerContainer 생성
		SqsMessageListenerContainer<Object> container = factory.createContainer("mejai-renewal-sqs");

		// MessageListener 설정
		container.setMessageListener(new MyMessageListener(objectMapper));
		return container;
	}

}
