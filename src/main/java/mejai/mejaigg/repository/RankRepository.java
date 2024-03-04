package mejai.mejaigg.repository;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import mejai.mejaigg.domain.Rank;

@Repository
@RequiredArgsConstructor
public class RankRepository {
	private final EntityManager em;

	public void save(Rank rank) {
		em.persist(rank);
	}

	public Rank findOne(Long id) {
		return em.find(Rank.class, id);
	}

	public Rank findBySeason(Long season) {
		return em.createQuery("select r from Rank r where r.season = :season", Rank.class)
			.setParameter("season", season)
			.getSingleResult();
	}
}
