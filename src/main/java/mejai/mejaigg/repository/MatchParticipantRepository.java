package mejai.mejaigg.repository;

import jakarta.persistence.EntityManager;
import mejai.mejaigg.domain.MatchParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchParticipantRepository extends JpaRepository<MatchParticipant, Long>{
}
