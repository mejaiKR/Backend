package mejai.mejaigg.match_participant.repository;

import mejai.mejaigg.match_participant.domain.MatchParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MatchParticipantRepository extends JpaRepository<MatchParticipant, Long> {
	List<MatchParticipant> findMatchesByPuuid(String puuid);

	List<MatchParticipant> findByPuuidAndMatch_GameCreationGreaterThanEqual(String puuid, LocalDateTime gameCreation);
}
