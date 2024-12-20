package mejai.mejaigg.global.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import mejai.mejaigg.global.util.YearMonthToEpochUtil;
import mejai.mejaigg.summoner.dto.request.SummonerStreakRequest;

public class FutureDateValidator implements ConstraintValidator<FutureDate, SummonerStreakRequest> {
	@Override
	public boolean isValid(SummonerStreakRequest value, ConstraintValidatorContext context) {
		int currentYear = YearMonthToEpochUtil.getNowYear();
		int currentMonth = YearMonthToEpochUtil.getNowMonth();

		if (value.getYear() < currentYear) {
			return true;
		}
		if (value.getYear() == currentYear && value.getMonth() <= currentMonth) {
			return true;
		}
		return false;
	}
}
