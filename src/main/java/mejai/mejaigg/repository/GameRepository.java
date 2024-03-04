package mejai.mejaigg.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import mejai.mejaigg.domain.Game;

@Repository
@RequiredArgsConstructor
public class GameRepository {
	private final EntityManager em;

	public void save(Game game) {
		if (game.getGameId() == null) {
			em.persist(game);
		} else {
			em.merge(game);
		}
	}

	public Game findOne(Long matchId) {
		return em.find(Game.class, matchId);
	}

	public List<Game> findAll() {
		return em.createQuery("select g from Game g", Game.class)
			.getResultList();
	}

}
