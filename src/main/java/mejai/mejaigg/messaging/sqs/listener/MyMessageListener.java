package mejai.mejaigg.messaging.sqs.listener;

import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.awspring.cloud.sqs.listener.MessageListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mejai.mejaigg.matchstreak.service.StreakService;
import mejai.mejaigg.messaging.sqs.config.AwsProperties;
import mejai.mejaigg.riot.exception.ClientErrorCode;
import mejai.mejaigg.riot.exception.ClientException;
import mejai.mejaigg.summoner.dto.request.SummonerProfileRequest;
import mejai.mejaigg.summoner.dto.request.SummonerStreakRequest;
import mejai.mejaigg.summoner.service.SummonerService;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;

@RequiredArgsConstructor
@Component
@Slf4j
public class MyMessageListener implements MessageListener<Object> {
	private final ObjectMapper objectMapper;
	private final SummonerService summonerService;
	private final StreakService streakService;
	private final SqsAsyncClient sqsAsyncClient;
	private final AwsProperties awsProperties;

	@Override
	public void onMessage(Message<Object> message) {
		try {
			String payload = (String)message.getPayload(); // 메시지 페이로드를 String으로 변환

			// 메시지 타입에 따라 처리
			if (payload.contains("year") && payload.contains("month")) {
				log.info("Streak message received.");
				SummonerStreakRequest request = objectMapper.readValue(payload, SummonerStreakRequest.class);
				streakService.renewalStreak(
					request.getSummonerName(),
					request.getTag(),
					request.getYear(),
					request.getMonth()
				);
			} else {
				log.info("Profile message received.");
				SummonerProfileRequest request = objectMapper.readValue(payload, SummonerProfileRequest.class);
				summonerService.renewalSummonerProfileByNameTag(
					request.getSummonerName(),
					request.getTag()
				);
			}

			// ACK 처리
			String receiptHandle = message.getHeaders().get("Sqs_ReceiptHandle", String.class);
			if (receiptHandle != null) {
				deleteMessage(receiptHandle);
				log.info("Message deleted successfully.");
			} else {
				log.warn("Receipt handle not found for the message.");
			}

		} catch (ClientException e) {
			// 특정 에러 코드가 TOO_MANY_REQUESTS인 경우에만 처리
			if (e.getClientErrorCode() == ClientErrorCode.TOO_MANY_REQUESTS) {
				// TOO_MANY_REQUESTS 에러에 대한 처리 로직
				log.info("Too many requests. Waiting for a while.");
				SqsListenerControlService.requestStop();
				return;
			}
			String receiptHandle = message.getHeaders().get("Sqs_ReceiptHandle", String.class);
			deleteMessage(receiptHandle);
		} catch (Exception e) {
			log.warn("Failed to process message: " + e.getMessage());
			String receiptHandle = message.getHeaders().get("Sqs_ReceiptHandle", String.class);
			deleteMessage(receiptHandle);
		}
	}

	private void deleteMessage(String receiptHandle) {
		// 메시지 삭제 요청
		DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
			.queueUrl(awsProperties.getSqsName()) // SQS 큐 URL
			.receiptHandle(receiptHandle)
			.build();

		sqsAsyncClient.deleteMessage(deleteMessageRequest).whenComplete((result, exception) -> {
			if (exception != null) {
				log.error("Failed to delete message: {}", exception.getMessage());
			} else {
				log.info("Message successfully deleted from the queue.");
			}
		});
	}
}
