package mejai.mejaigg.summoner.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import mejai.mejaigg.summoner.domain.Summoner;

public interface SummonerRepository extends JpaRepository<Summoner, Long>, SummonerCustom {

	Optional<Summoner> findById(Long id);

	Optional<Summoner> findBySummonerNameAndTagLineAllIgnoreCase(String name, String tag);

	Optional<Summoner> findByNormalizedSummonerNameAndNormalizedTagLine(String name, String tag);

	List<Summoner> findBySummonerNameContainingAllIgnoreCaseOrderBySummonerNameDesc(String summonerName, Limit limit);
}
