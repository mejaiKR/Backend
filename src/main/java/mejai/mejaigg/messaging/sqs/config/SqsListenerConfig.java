package mejai.mejaigg.messaging.sqs.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import io.awspring.cloud.sqs.listener.SqsMessageListenerContainer;
import io.awspring.cloud.sqs.listener.acknowledgement.handler.AcknowledgementMode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mejai.mejaigg.messaging.sqs.listener.MyMessageListener;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SqsListenerConfig {
	private final MyMessageListener myMessageListener;
	private final AwsProperties awsProperties;

	@Bean
	public SqsMessageListenerContainer<Object> sqsMessageListenerContainer(SqsAsyncClient sqsAsyncClient) {
		SqsMessageListenerContainerFactory<Object> factory = SqsMessageListenerContainerFactory.builder()
			.sqsAsyncClient(sqsAsyncClient)
			.configure(options -> options
				.acknowledgementMode(AcknowledgementMode.MANUAL) // 수동 확인 모드
				.maxConcurrentMessages(10) // 최대 동시 메시지 처리 수
				.pollTimeout(Duration.ofSeconds(10))) // 폴링 타임아웃
			.build();
		// SqsMessageListenerContainer 생성
		SqsMessageListenerContainer<Object> container = factory.createContainer(awsProperties.getSqsName());

		// MessageListener 설정
		container.setMessageListener((message) -> {
			try {
				myMessageListener.onMessage(message); // 메시지 처리 명시적으로 메시지 삭제 진행
			} catch (Exception e) {
				log.warn("Failed to process message: " + e.getMessage());
			}
		});
		return container;
	}

}
