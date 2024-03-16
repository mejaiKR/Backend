package mejai.mejaigg.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import mejai.mejaigg.domain.User;

@Repository
@RequiredArgsConstructor
public class UserRepository {
	private final EntityManager em;

	public void save(User user) {
		em.persist(user);
	}

	public User findOneWithRank(String puuid){
		return em.createQuery(
			"select u from User u " +
				"join fetch u.ranks r " +
				"where u.puuid = :puuid",
				User.class)
			.setParameter("puuid", puuid)
			.getSingleResult();
	}
}
