package mejai.mejaigg.app.user.domain;

public enum SocialType {
	APPLE("apple"),
	KAKAO("kakao"),
	GOOGLE("google");

	private final String type;

	SocialType(String type) {
		this.type = type;
	}

	public static SocialType of(String type) {
		for (SocialType socialType : values()) {
			if (socialType.type.equals(type)) {
				return socialType;
			}
		}
		throw new IllegalArgumentException("Unsupported type: " + type);
	}
}
