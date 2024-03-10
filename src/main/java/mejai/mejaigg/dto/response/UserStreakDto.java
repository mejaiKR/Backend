package mejai.mejaigg.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserStreakDto {
	private String date;
	private int gameCount;
	private int winCount;
	private int loseCount;
}
