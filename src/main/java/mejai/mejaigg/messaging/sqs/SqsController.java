package mejai.mejaigg.messaging.sqs;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mejai.mejaigg.messaging.sqs.config.AwsProperties;
import mejai.mejaigg.messaging.sqs.listener.SqsListenerControlService;
import mejai.mejaigg.messaging.sqs.sender.MessageSender;
import mejai.mejaigg.summoner.dto.request.UserProfileRequest;
import mejai.mejaigg.summoner.dto.request.UserStreakRequest;
import mejai.mejaigg.summoner.dto.response.UserProfileDto;
import mejai.mejaigg.summoner.dto.response.UserStreakDto;

@RestController
@RequiredArgsConstructor
@Tag(name = "SQS", description = "SQS 테스트")
@RequestMapping("/sqs-test")
public class SqsController {

	private final MessageSender messageSender;
	private final ObjectMapper objectMapper;
	private final AwsProperties awsProperties;

	@GetMapping("/post")
	@Operation(summary = "SQS 테스트", description = "SQS 메시지 전송 테스트")
	public String test() {
		messageSender.sendMessage(awsProperties.getSqsName(), "Hello, SQS!");
		return "success";
	}

	@PostMapping("/profile")
	@Operation(summary = "소환사 정보 조회", description = "주어진 소환사 ID로 프로필 정보를 조회합니다.")
	public UserProfileDto profile(UserProfileRequest request) throws Exception {
		messageSender.sendMessage(awsProperties.getSqsName(), objectMapper.writeValueAsString(request));
		return null;
	}

	@PostMapping("/users/streak")
	@Operation(summary = "소환사 게임 횟수 및 승패 조회", description = "소환사가 특정 기간 동안 진행한 게임 횟수 및 승패를 조회합니다.")
	public List<UserStreakDto> streak(@RequestBody UserStreakRequest request) throws Exception {
		messageSender.sendMessage(awsProperties.getSqsName(), objectMapper.writeValueAsString(request));
		return null;
	}

	@PostMapping("/start")
	public String startListener() {
		SqsListenerControlService.requestStart();
		return "SQS Listener started.";
	}

	@PostMapping("/stop")
	public String stopListener() {
		SqsListenerControlService.requestStop();
		return "SQS Listener stopped.";
	}
}
