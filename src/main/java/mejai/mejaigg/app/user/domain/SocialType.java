package mejai.mejaigg.app.user.domain;

public enum SocialType {
	GOOGLE("google"),
	KAKAO("kakao");

	private final String type;

	SocialType(String type) {
		this.type = type;
	}
}
