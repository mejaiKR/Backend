package mejai.mejaigg.messaging.sqs.listener;

import org.springframework.stereotype.Service;

import io.awspring.cloud.sqs.listener.SqsMessageListenerContainer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SqsListenerControlService {
	private final SqsMessageListenerContainer<Object> sqsMessageListenerContainer;

	public void stopListener() {
		sqsMessageListenerContainer.stop();
		log.info("SQS Listener has been stopped.");
	}

	public void startListener() {
		sqsMessageListenerContainer.start();
		log.info("SQS Listener has been started.");
	}

}
