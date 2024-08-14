package mejai.mejaigg.rank.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

// 복합키 클래스
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class RankId implements Serializable {
	private String id;
	private String queueType;
}
