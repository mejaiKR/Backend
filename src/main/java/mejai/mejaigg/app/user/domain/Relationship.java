package mejai.mejaigg.app.user.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;

@Getter
public enum Relationship {
	LOVER("애인"),
	FRIEND("친구"),
	MATE("동료"),
	CHILD("자녀"),
	RIVAL("라이벌"),
	STREAMER("스트리머");

	private final String relationship;

	Relationship(String relationship) {
		this.relationship = relationship;
	}

	@JsonCreator(mode = JsonCreator.Mode.DELEGATING)
	public static Relationship of(String relationship) {
		for (Relationship r : values()) {
			if (r.relationship.equals(relationship)) {
				return r;
			}
		}
		throw new IllegalArgumentException("알 수 없는 관계입니다.");
	}
}
