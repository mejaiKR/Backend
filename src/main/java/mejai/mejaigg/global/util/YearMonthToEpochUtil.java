package mejai.mejaigg.global.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class YearMonthToEpochUtil {
	// Private constructor to prevent instantiation
	private YearMonthToEpochUtil() {
	}

	public static long convertToEpochSeconds(String yearMonthStr) {
		return Instant.parse(yearMonthStr + "-01T00:00:00Z").getEpochSecond();
	}

	public static String convertToYearMonthDay(long epochSecond) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return Instant.ofEpochSecond(epochSecond).atZone(ZoneId.of("UTC")).format(formatter);
	}

	public static long convertToEpochSeconds(int year, int month) {
		return YearMonth.of(year, month).atDay(1).atStartOfDay(ZoneId.of("UTC")).toEpochSecond();
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

	public static long addDayEpochSecond(String yearMonthStr, int day) {
		Instant originalInstant = Instant.parse(yearMonthStr + "-01T00:00:00Z");
		ZonedDateTime zonedDateTime = originalInstant.atZone(ZoneId.of("UTC"));
		// ZonedDateTime 객체에 일 더하기
		ZonedDateTime oneDayLater = zonedDateTime.plusDays(day);
		// ZonedDateTime 객체를 Instant 객체로 변환
		Instant oneDayLaterInstant = oneDayLater.toInstant();
		// Instant 객체를 EpochSecond로 변환
		return oneDayLaterInstant.getEpochSecond();
	}

	public static int getDayWithYearMonth(String yearMonthStr) {
		Instant originalInstant = Instant.parse(yearMonthStr + "-01T00:00:00Z");
		ZonedDateTime zonedDateTime = originalInstant.atZone(ZoneId.of("UTC"));
		return zonedDateTime.toLocalDate().lengthOfMonth();
	}

	public static int findMaxDay(int year, int month) {
		return YearMonth.of(year, month).lengthOfMonth();
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

	//return format: YYYY-MM-DD
	public static String getNowYearMonthDay() {
		LocalDate currentDate = LocalDate.now();

		// yyyy-MM-dd 형식으로 날짜를 포매팅합니다.
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		return currentDate.format(formatter);
	}

	public static int getNowDay() {
		return LocalDate.now().getDayOfMonth();
	}
}
