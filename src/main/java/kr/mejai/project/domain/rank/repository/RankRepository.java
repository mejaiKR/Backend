package kr.mejai.project.domain.rank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.mejai.project.domain.rank.domain.Rank;
import kr.mejai.project.domain.rank.domain.RankId;

public interface RankRepository extends JpaRepository<Rank, RankId> {
}
