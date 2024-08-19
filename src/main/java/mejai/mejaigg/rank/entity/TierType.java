package mejai.mejaigg.rank.entity;

import lombok.Getter;

@Getter
public enum TierType {
	UNRANKED("UNRANKED"),
	IRON("IRON"),
	BRONZE("BRONZE"),
	SILVER("SILVER"),
	GOLD("GOLD"),
	PLATINUM("PLATINUM"),
	DIAMOND("DIAMOND"),
	MASTER("MASTER"),
	GRANDMASTER("GRANDMASTER"),
	CHALLENGER("CHALLENGER");
	private final String name;

	TierType(String name) {
		this.name = name;
	}
}
