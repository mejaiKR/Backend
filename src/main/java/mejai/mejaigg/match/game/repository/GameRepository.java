package mejai.mejaigg.match.game.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mejai.mejaigg.match.game.entity.Game;

public interface GameRepository extends JpaRepository<Game, String> {
}
