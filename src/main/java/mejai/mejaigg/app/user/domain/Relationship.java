package mejai.mejaigg.app.user.domain;

public enum Relationship {
	LOVER("애인"),
	FRIEND("친구"),
	MATE("동료"),
	CHILD("자녀"),
	RIVAL("라이벌"),
	STREAMER("스트리머");

	private String relationship;

	Relationship(String relationship) {
		this.relationship = relationship;
	}
}
