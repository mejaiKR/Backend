package mejai.mejaigg.rank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mejai.mejaigg.rank.domain.Rank;
import mejai.mejaigg.rank.domain.RankId;

public interface RankRepository extends JpaRepository<Rank, RankId> {
}
