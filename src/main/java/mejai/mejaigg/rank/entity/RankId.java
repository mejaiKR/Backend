package mejai.mejaigg.rank.entity;

import java.io.Serializable;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

// 복합키 클래스
@AllArgsConstructor
@NoArgsConstructor
public class RankId implements Serializable {
	private String puuid;
	private String queueType;

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		RankId rankId = (RankId)obj;
		return Objects.equals(puuid, rankId.puuid) && Objects.equals(queueType, rankId.queueType);
	}

	@Override
	public int hashCode() {
		return Objects.hash(puuid, queueType);
	}
}
