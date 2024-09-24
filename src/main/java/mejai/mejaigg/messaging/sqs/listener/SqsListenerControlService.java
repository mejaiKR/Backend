package mejai.mejaigg.messaging.sqs.listener;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import io.awspring.cloud.sqs.listener.SqsMessageListenerContainer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SqsListenerControlService implements ApplicationContextAware {
	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext ctx) {
		context = ctx;
	}

	public static void requestStop() {
		SqsMessageListenerContainer<?> container = context.getBean(SqsMessageListenerContainer.class);
		container.stop();
		log.info("SQS Listener has been stopped due to an error during message processing.");
	}

	public static void requestStart() {
		SqsMessageListenerContainer<?> container = context.getBean(SqsMessageListenerContainer.class);
		container.start();
		log.info("SQS Listener has been started.");
	}
}
