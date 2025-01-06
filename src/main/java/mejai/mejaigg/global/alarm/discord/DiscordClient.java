package mejai.mejaigg.global.alarm.discord;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
	name = "discord-client",
	url = "${discord.webhook-url}")
public interface DiscordClient {

	@PostMapping()
	void sendAlarm(@RequestBody DiscordMessage message);
}
