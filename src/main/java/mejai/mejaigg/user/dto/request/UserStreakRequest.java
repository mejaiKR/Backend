package mejai.mejaigg.user.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import mejai.mejaigg.common.validation.FutureDate;

@Data
@FutureDate
public class UserStreakRequest {

	@NotBlank
	@Size(min = 1, max = 30)
	private String id;

	@NotBlank
	@Size(min = 2, max = 10)
	private String tag = "Kr1";

	@Min(2021)
	private int year;

	@Min(1)
	@Max(12)
	private int month;

}
