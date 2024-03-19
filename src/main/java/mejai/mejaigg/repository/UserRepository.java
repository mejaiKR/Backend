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

	public User findOne(String puuid) {
		return em.find(User.class, puuid);
	}

	public User findOneWithNameAndTag(String name, String tag) {
		List<User> users = em.createQuery(
				"select u from User u "
					+ "where u.summonerName = :name "
					+ "and u.tagLine = :tag",
				User.class)
			.setParameter("name", name.toLowerCase())
			.setParameter("tag", tag.toLowerCase())
			.getResultList();
		if (users.isEmpty())
			return null;
		else
			return users.get(0);
	}
}
