package kr.mejai.project.external.sqs;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.awspring.cloud.sqs.operations.SendResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.mejai.project.domain.summoner.dto.request.SummonerProfileRequest;
import kr.mejai.project.domain.summoner.dto.request.SummonerStreakRequest;
import kr.mejai.project.domain.summoner.dto.response.SummonerProfileResponse;
import kr.mejai.project.domain.summoner.dto.response.SummonerStreakResponse;
import kr.mejai.project.external.sqs.listener.SqsListenerControlService;
import kr.mejai.project.external.sqs.sender.MessageSender;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "SQS", description = "SQS 테스트")
@RequestMapping("/sqs-test")
public class SqsController {

	private final MessageSender messageSender;

	@GetMapping("/post")
	@Operation(summary = "SQS 테스트", description = "SQS 메시지 전송 테스트")
	public SendResult<String> test() {
		return messageSender.sendMessage("Hello, SQS!");
	}

	@PostMapping("/profile")
	@Operation(summary = "소환사 정보 조회", description = "주어진 소환사 ID로 프로필 정보를 조회합니다.")
	public SummonerProfileResponse profile(SummonerProfileRequest request) throws Exception {
		messageSender.sendMessage(request);
		return null;
	}

	@PostMapping("/users/streak")
	@Operation(summary = "소환사 게임 횟수 및 승패 조회", description = "소환사가 특정 기간 동안 진행한 게임 횟수 및 승패를 조회합니다.")
	public List<SummonerStreakResponse> streak(@RequestBody SummonerStreakRequest request) throws Exception {
		messageSender.sendMessage(request);
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
