package mejai.mejaigg.dto.response;

import java.time.format.DateTimeFormatter;

import lombok.Data;
import mejai.mejaigg.domain.MatchDateStreak;

@Data
public class UserStreakDto {
	private String date;
	private int gameCount;
	private String imageUrl;
	// private int rankCount;

	public void setDummy(int year, int month, int day) {
		this.date = year + "-" + month + "-" + day;
		this.gameCount = 10;
		// this.rankCount = 5;
	}

	public void setByMatchDateStreak(MatchDateStreak matchDateStreak, String resourceUrl) {
		this.date = matchDateStreak.getDate().toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		this.gameCount = matchDateStreak.getMatches().size();
		this.imageUrl = resourceUrl + "mejaiStack/" + gameCount + ".svg";
		// this.rankCount = matchDateStreak.getMatches().size();
	}
}
