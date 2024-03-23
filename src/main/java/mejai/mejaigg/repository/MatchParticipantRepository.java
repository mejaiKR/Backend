package mejai.mejaigg.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mejai.mejaigg.domain.MatchParticipant;

public interface MatchParticipantRepository extends JpaRepository<MatchParticipant, Long> {
}
