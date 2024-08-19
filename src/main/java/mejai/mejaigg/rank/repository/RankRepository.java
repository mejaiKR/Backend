package mejai.mejaigg.rank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mejai.mejaigg.rank.entity.Rank;
import mejai.mejaigg.rank.entity.RankId;

public interface RankRepository extends JpaRepository<Rank, RankId> {
}
