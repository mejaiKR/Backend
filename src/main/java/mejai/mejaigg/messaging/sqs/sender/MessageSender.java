package mejai.mejaigg.messaging.sqs.sender;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.awspring.cloud.sqs.operations.SendResult;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import mejai.mejaigg.messaging.sqs.config.AwsProperties;

@Service
@RequiredArgsConstructor
public class MessageSender {

	private final SqsTemplate sqsTemplate;
	private final AwsProperties awsProperties;
	private final ObjectMapper objectMapper;

	public SendResult<String> sendMessage(String message) {
		return sqsTemplate.send(awsProperties.getSqsName(), message);
	}

	public <T> SendResult<String> sendMessage(T message) {
		String json = "";
		try {
			json = objectMapper.writeValueAsString(message);
		} catch (IOException e) {
			throw new IllegalArgumentException("요청에 실패했습니다.");
		}
		return sqsTemplate.send(awsProperties.getSqsName(), json);
	}
}
