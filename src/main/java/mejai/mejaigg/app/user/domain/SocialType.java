package mejai.mejaigg.app.user.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum SocialType {
	APPLE("apple"),
	KAKAO("kakao"),
	GOOGLE("google");

	private final String type;

	SocialType(String type) {
		this.type = type;
	}

	@JsonCreator(mode = JsonCreator.Mode.DELEGATING)
	public static SocialType of(String type) {
		for (SocialType socialType : values()) {
			if (socialType.type.equals(type)) {
				return socialType;
			}
		}
		throw new IllegalArgumentException("Unsupported type: " + type);
	}
}
