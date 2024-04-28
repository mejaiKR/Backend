package mejai.mejaigg.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserProfileRequest {

	@NotBlank
	@Size(min = 1, max = 30)
	private String id;

	@NotBlank
	@Size(min = 2, max = 10)
	private String tag = "Kr1";
}
