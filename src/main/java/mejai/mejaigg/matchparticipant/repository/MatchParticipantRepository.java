package mejai.mejaigg.matchparticipant.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mejai.mejaigg.matchparticipant.domain.MatchParticipant;

public interface MatchParticipantRepository extends JpaRepository<MatchParticipant, Long> {
	List<MatchParticipant> findMatchesByPuuid(String puuid);

	List<MatchParticipant> findByPuuidAndMatch_GameCreationBetween(
		String puuid,
		LocalDateTime startDate,
		LocalDateTime endDate
	);
}
