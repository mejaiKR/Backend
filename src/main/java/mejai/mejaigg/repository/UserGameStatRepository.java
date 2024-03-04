package mejai.mejaigg.repository;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import mejai.mejaigg.domain.UserGameStat;

@Repository
@RequiredArgsConstructor
public class UserGameStatRepository {
	private final EntityManager em;

	public void save(UserGameStat userGameStat) {
		em.persist(userGameStat);
	}

	public UserGameStat findOne(Long id) {
		return em.find(UserGameStat.class, id);
	}

	public UserGameStat findByGameId(Long puuid) {
		return em.createQuery("select ugs from UserGameStat ugs where ugs.puuid = :puuid", UserGameStat.class)
			.setParameter("puuid", puuid)
			.getSingleResult();
	}
}
