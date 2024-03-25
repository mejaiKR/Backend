package mejai.mejaigg.common;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class YearMonthToEpochUtil {
	// Private constructor to prevent instantiation
	private YearMonthToEpochUtil() {
	}

	public static long convertToEpochSeconds(String yearMonthStr) {
		return Instant.parse(yearMonthStr + "-01T00:00:00Z").getEpochSecond();
	}

	public static long addMonthEpochSecond(String yearMonthStr, int month) {
		Instant originalInstant = Instant.parse(yearMonthStr + "-01T00:00:00Z");
		ZonedDateTime zonedDateTime = originalInstant.atZone(ZoneId.of("UTC"));
		// ZonedDateTime 객체에 한 달 더하기
		ZonedDateTime oneMonthLater = zonedDateTime.plusMonths(1);
		// ZonedDateTime 객체를 Instant 객체로 변환
		Instant oneMonthLaterInstant = oneMonthLater.toInstant();
		// Instant 객체를 EpochSecond로 변환
		return oneMonthLaterInstant.getEpochSecond();

	}

}
