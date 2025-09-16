package kr.mejai.project.common.scheduler;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import kr.mejai.project.external.sqs.listener.SqsListenerControlService;

@Configuration
@EnableScheduling
@Component
public class CronConfig {
	@Scheduled(cron = "0 * * * * *")  // 1분마다 실행
	public void cron() {
		SqsListenerControlService.requestStart();
	}
}
