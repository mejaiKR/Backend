package mejai.mejaigg.dto.response;

import lombok.Data;

@Data
public class UserStreakDto {
	private String date;
	private int gameCount;
	private int winCount;
	private int loseCount;
}
