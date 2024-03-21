package mejai.mejaigg.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import mejai.mejaigg.domain.Game;

public interface GameRepository extends JpaRepository<Game, String> {
}
