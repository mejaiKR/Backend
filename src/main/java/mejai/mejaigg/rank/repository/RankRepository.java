package mejai.mejaigg.rank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mejai.mejaigg.rank.entity.Rank;

public interface RankRepository extends JpaRepository<Rank, Long> {
}
