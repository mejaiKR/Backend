package mejai.mejaigg.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import mejai.mejaigg.common.util.YearMonthToEpochUtil;
import mejai.mejaigg.user.dto.request.UserStreakRequest;

public class FutureDateValidator implements ConstraintValidator<FutureDate, UserStreakRequest> {
	@Override
	public boolean isValid(UserStreakRequest value, ConstraintValidatorContext context) {
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
