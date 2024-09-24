package mejai.mejaigg.scheduler;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import mejai.mejaigg.messaging.sqs.listener.SqsListenerControlService;

@Configuration
@EnableScheduling
@Component
public class CronConfig {
	@Scheduled(cron = "0 * * * * *")  // 1분마다 실행
	public void test() {
		SqsListenerControlService.requestStart();
	}
}
