package mejai.mejaigg.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import mejai.mejaigg.domain.MatchParticipant;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MatchParticipantRepository {
	private final EntityManager em;

	public void save(MatchParticipant matchParticipant) {
		if (matchParticipant.getId() == null) {
			em.persist(matchParticipant);
		} else {
			em.merge(matchParticipant);
		}
	}
}
