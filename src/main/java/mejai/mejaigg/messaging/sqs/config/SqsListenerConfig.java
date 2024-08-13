package mejai.mejaigg.messaging.sqs.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import io.awspring.cloud.sqs.listener.SqsMessageListenerContainer;
import mejai.mejaigg.messaging.sqs.listener.MyMessageListener;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Configuration
public class SqsListenerConfig {
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
		container.setMessageListener(new MyMessageListener());
		return container;
	}

}
