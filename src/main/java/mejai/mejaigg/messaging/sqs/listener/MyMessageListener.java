package mejai.mejaigg.messaging.sqs.listener;

import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.awspring.cloud.sqs.listener.MessageListener;
import lombok.RequiredArgsConstructor;
import mejai.mejaigg.summoner.dto.request.UserProfileRequest;
import mejai.mejaigg.summoner.dto.request.UserStreakRequest;

@RequiredArgsConstructor
@Component
public class MyMessageListener implements MessageListener<Object> {
	private final ObjectMapper objectMapper;
	// private final ProfileService profileService;

	@Override
	public void onMessage(Message<Object> message) {
		try {
			String payload = (String)message.getPayload(); // 메시지 페이로드를 String으로 변환

			// 메시지 타입에 따라 처리
			if (payload.contains("year") && payload.contains("month")) {
				UserStreakRequest request = objectMapper.readValue(payload, UserStreakRequest.class);
				handleUserStreakRequest(request);
			} else {
				UserProfileRequest request = objectMapper.readValue(payload, UserProfileRequest.class);
				handleUserProfileRequest(request);
			}
		} catch (Exception e) {
			// 예외 처리
			System.err.println("Failed to process message: " + e.getMessage());
		}
	}

	private void handleUserStreakRequest(UserStreakRequest request) {
		System.out.println("Handling UserStreakRequest: " + request);
		// 비즈니스 로직 처리
		// profileService.getUserProfileByNameTag(request.getId(), request.getTag());
	}

	private void handleUserProfileRequest(UserProfileRequest request) {
		System.out.println("Handling UserProfileRequest: " + request);
		// 비즈니스 로직 처리
	}
}
