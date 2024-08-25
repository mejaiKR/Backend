package mejai.mejaigg.messaging.sqs.sender;

import org.springframework.stereotype.Service;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageSender {

	private final SqsTemplate sqsTemplate;

	public void sendMessage(String queueName, String message) {
		sqsTemplate.send(queueName, message);
	}
}
