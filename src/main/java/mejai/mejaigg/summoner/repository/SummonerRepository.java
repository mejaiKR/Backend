package mejai.mejaigg.summoner.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import mejai.mejaigg.summoner.domain.Summoner;

public interface SummonerRepository extends JpaRepository<Summoner, String>, SummonerCustom {

	Optional<Summoner> findById(String puuid);

	Optional<Summoner> findBySummonerNameAndTagLineAllIgnoreCase(String name, String tag);
}
