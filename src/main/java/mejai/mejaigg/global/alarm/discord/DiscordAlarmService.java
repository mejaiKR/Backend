package mejai.mejaigg.global.alarm.discord;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mejai.mejaigg.global.alarm.AlarmService;

@Service
@RequiredArgsConstructor
@Slf4j
@Profile("prod")
public class DiscordAlarmService implements AlarmService {

	private final DiscordClient discordClient;

	public void sendAlarm(Exception e, String context) {
		try {
			discordClient.sendAlarm(createMessage(e, context));
		} catch (Exception discordEx) {
			log.error("Failed to send discord alarm: {}", discordEx.getMessage());
		}
	}

	private DiscordMessage createMessage(Exception e, String context) {
		String stackTrace = getStackTrace(e);
		// 너무 길 경우 자르기(디스코드 Embed 제한을 고려)
		if (stackTrace.length() > 2000) {
			stackTrace = stackTrace.substring(0, 2000) + "\n... (생략)";
		}

		var message = DiscordMessage.Embed.builder()
			.title("ℹ️ 에러 정보")
			.description(
				"### 🕖 발생 시간\n" + LocalDateTime.now() + "\n\n"
					+ "### 🏷️ 컨텍스트\n" + context + "\n\n"
					+ "### 📄 Stack Trace\n"
					+ "```\n" + stackTrace + "\n```")
			.build();
		return DiscordMessage.builder()
			.content("🚨 에러 발생 알림: " + context)
			.embeds(List.of(message))
			.build();
	}

	private String getStackTrace(Exception e) {
		StringWriter stringWriter = new StringWriter();
		e.printStackTrace(new PrintWriter(stringWriter));
		return stringWriter.toString();
	}
}
