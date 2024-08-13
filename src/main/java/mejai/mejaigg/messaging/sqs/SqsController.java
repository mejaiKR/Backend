package mejai.mejaigg.messaging.sqs;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import mejai.mejaigg.messaging.sqs.sender.MessageSender;

@RestController
@RequiredArgsConstructor
@Tag(name = "SQS", description = "SQS 테스트")
public class SqsController {

	private final MessageSender messageSender;

	@GetMapping("/sqs-test")
	@Operation(summary = "SQS 테스트", description = "SQS 메시지 전송 테스트")
	public String test() {
		messageSender.sendMessage("mejai-renewal-sqs", "Hello, SQS!");
		return "success";
	}
}
