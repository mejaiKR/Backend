package mejai.mejaigg.global.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneOffset;

public class YearMonthToEpochUtil {
	// Private constructor to prevent instantiation
	private YearMonthToEpochUtil() {
	}

	public static long convertToEpochSeconds(LocalDate localDate) {
		// LocalDate를 LocalDateTime으로 변환하고, 자정을 기준으로 시간 설정
		LocalDateTime localDateTime = localDate.atStartOfDay();

		// UTC 기준으로 Instant로 변환하고, 에포크 초로 변환
		return localDateTime.toInstant(ZoneOffset.UTC).getEpochSecond();
	}
	
	public static int getNowEpochSecond() {
		return (int)Instant.now().getEpochSecond();
	}

	public static int getNowYear() {
		return YearMonth.now().getYear();
	}

	public static int getNowMonth() {
		return YearMonth.now().getMonthValue();
	}

	public static String getNowYearMonth() {
		return YearMonth.now().toString();
	}

	public static int getNowDay() {
		return LocalDate.now().getDayOfMonth();
	}
}
