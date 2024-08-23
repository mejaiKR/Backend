package mejai.mejaigg.rank.domain;

import lombok.Getter;

@Getter
public enum RankType {
	I("I"),
	II("II"),
	III("III"),
	IV("IV"),
	V("V");
	private final String name;

	RankType(String name) {
		this.name = name;
	}
}
