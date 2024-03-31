package mejai.mejaigg.dto.response;

import lombok.Data;

@Data
public class UserStreakDto {
	private String date;
	private int gameCount;
	private int winCount;
	private int loseCount;

	public void setDummy(int year, int month, int day) {
		this.date = year + "-" + month + "-" + day;
		this.gameCount = 10;
		this.winCount = 5;
		this.loseCount = 5;
	}
}
