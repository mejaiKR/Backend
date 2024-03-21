package mejai.mejaigg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mejai.mejaigg.domain.Rank;


public interface RankRepository extends JpaRepository<Rank, Long> {
}
