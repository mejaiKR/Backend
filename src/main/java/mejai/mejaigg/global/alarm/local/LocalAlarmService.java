package mejai.mejaigg.global.alarm.local;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import mejai.mejaigg.global.alarm.AlarmService;

@Service
@Slf4j
@Profile("!prod")
public class LocalAlarmService implements AlarmService {

	@Override
	public void sendAlarm(Exception e, String context) {
		log.info("Local Alarm: {}", context);
	}
}
