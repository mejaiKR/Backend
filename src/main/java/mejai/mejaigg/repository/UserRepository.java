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

	public User findOne(Long id) {
		return em.find(User.class, id);
	}

	public User findByPuuid(String puuid) {
		return em.createQuery("select u from User u where u.puuid = :puuid", User.class)
			.setParameter("puuid", puuid)
			.getSingleResult();
	}

	public User findByAccountId(String accountId) {
		return em.createQuery("select u from User u where u.accountId = :accountId", User.class)
			.setParameter("accountId", accountId)
			.getSingleResult();
	}

	public User findBySummonerId(String summonerId) {
		return em.createQuery("select u from User u where u.summonerId = :summonerId", User.class)
			.setParameter("summonerId", summonerId)
			.getSingleResult();
	}

	public User findBySummonerName(String summonerName) {
		return em.createQuery("select u from User u where u.summonerName = :summonerName", User.class)
			.setParameter("summonerName", summonerName)
			.getSingleResult();
	}

	public List<User> findAll() {
		return em.createQuery("select u from User u", User.class).getResultList();
	}
}
