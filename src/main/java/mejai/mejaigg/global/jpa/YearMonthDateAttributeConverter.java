package mejai.mejaigg.global.jpa;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class YearMonthDateAttributeConverter implements
	AttributeConverter<YearMonth, String> {
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

	@Override
	public String convertToDatabaseColumn(
		YearMonth attribute) {
		return attribute != null ? attribute.format(FORMATTER) : null;
	}

	@Override
	public YearMonth convertToEntityAttribute(
		String dbData) {
		return dbData != null ? YearMonth.parse(dbData, FORMATTER) : null;
	}
}
