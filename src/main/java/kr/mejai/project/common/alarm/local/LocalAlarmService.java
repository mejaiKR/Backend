package kr.mejai.project.common.alarm.local;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import kr.mejai.project.common.alarm.AlarmService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Profile("!prod")
public class LocalAlarmService implements AlarmService {

	@Override
	public void sendAlarm(Exception e, String context) {
		log.info("Local Alarm: {}", context);
	}
}
