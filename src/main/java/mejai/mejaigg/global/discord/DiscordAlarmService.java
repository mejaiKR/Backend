package mejai.mejaigg.global.discord;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiscordAlarmService {

	private final DiscordClient discordClient;

	/**
	 * 예외 + 추가 설명(컨텍스트)을 받아서 디스코드로 알림을 보낸다.
	 * 컨텍스트에는 "SQS에서 에러", "Controller에서 에러" 등 출처나 부가정보를 적어둘 수 있다.
	 */
	public void sendDiscordAlarm(Exception e, String context) {
		// 실제로 DiscordClient를 통해 메시지를 전송
		discordClient.sendAlarm(createMessage(e, context));
	}

	/**
	 * DiscordMessage를 생성하는 메서드
	 * 여기서 e에 대한 stacktrace, 현재 시간 등의 정보를 담아서 전송할 수 있음
	 */
	private DiscordMessage createMessage(Exception e, String context) {
		// stack trace를 문자열로 변환
		String stackTrace = getStackTrace(e);
		// 너무 길 경우 자르기(디스코드 Embed 제한을 고려)
		if (stackTrace.length() > 2000) {
			stackTrace = stackTrace.substring(0, 2000) + "\n... (생략)";
		}

		return DiscordMessage.builder()
			.content("🚨 에러 발생 알림: " + context)  // context를 추가로 표기
			.embeds(
				List.of(
					DiscordMessage.Embed.builder()
						.title("ℹ️ 에러 정보")
						.description(
							"### 🕖 발생 시간\n" + LocalDateTime.now() + "\n\n"
								+ "### 🏷️ 컨텍스트\n" + context + "\n\n"
								+ "### 📄 Stack Trace\n"
								+ "```\n" + stackTrace + "\n```")
						.build()
				)
			)
			.build();
	}

	/**
	 * Exception의 stack trace를 문자열로 변환
	 */
	private String getStackTrace(Exception e) {
		StringWriter stringWriter = new StringWriter();
		e.printStackTrace(new PrintWriter(stringWriter));
		return stringWriter.toString();
	}
}
