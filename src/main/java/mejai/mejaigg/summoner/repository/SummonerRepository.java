package mejai.mejaigg.summoner.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import mejai.mejaigg.summoner.domain.Summoner;

public interface SummonerRepository extends JpaRepository<Summoner, Long>, SummonerCustom {

	Optional<Summoner> findById(Long id);

	Optional<Summoner> findBySummonerNameAndTagLineAllIgnoreCase(String name, String tag);
}
