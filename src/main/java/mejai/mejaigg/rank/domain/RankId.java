package mejai.mejaigg.rank.domain;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 복합키 클래스
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@AllArgsConstructor
@Getter
public class RankId implements Serializable {
	@Column(name = "id")
	private Long id;

	@Column(name = "queue_type")
	private String queueType;// ex) RANKED_SOLO_5x5 , RANKED_FLEX_SR
}
