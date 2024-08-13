package mejai.mejaigg.messaging.sqs.listener;

import org.springframework.stereotype.Component;

import io.awspring.cloud.sqs.annotation.SqsListener;

@Component
public class MessageListener {
	@SqsListener(value = "mejai-renewal-sqs", factory = "sqsListenerContainerFactory")
	public void listen(String message) {
		System.out.println("Received message: " + message);
	}
}
