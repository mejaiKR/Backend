package mejai.mejaigg.matchparticipant.service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mejai.mejaigg.matchparticipant.domain.MatchParticipant;
import mejai.mejaigg.matchparticipant.repository.MatchParticipantRepository;
import mejai.mejaigg.riot.service.RiotService;

@Service
@Slf4j
@RequiredArgsConstructor
public class MatchParticipantService {
	private final MatchParticipantRepository matchParticipantRepository;
	private final RiotService riotService;
	private final ObjectMapper objectMapper;

	public List<String> findUnsavedMatchIds(String puuid) {
		List<MatchParticipant> savedMatches = matchParticipantRepository.findMatchesByPuuid(puuid);
		Set<String> savedMatchIds = savedMatches.stream()
			.map(mp -> mp.getMatch().getMatchId())
			.collect(Collectors.toSet());

		List<String> matchIds = riotService.getMatchIds(puuid);

		// 저장된 matchId를 제외한 새로운 matchId 리스트 생성
		return matchIds.stream()
			.filter(id -> !savedMatchIds.contains(id))
			.toList();
	}

	public void createMatchParticipants(JsonNode participantsNode, Long matchId) {
		if (!participantsNode.isArray()) {
			return;
		}
		for (JsonNode participantNode : participantsNode) {
			try {
				MatchParticipant matchParticipant = MatchParticipant.parseJsonData(participantNode, matchId);
				matchParticipantRepository.save(matchParticipant);
			} catch (Exception e) {
				log.error("MatchParticipant parsing error : {}", e.getMessage());
			}
		}
	}

	public List<MatchParticipant> findMatchesOneWeek(String puuid) {
		// 이번주 월요일 ~ 오늘까지의 데이터만 조회
		LocalDateTime today = LocalDateTime.now();
		LocalDateTime monday = today.with(DayOfWeek.MONDAY)
			.withHour(0)
			.withMinute(0)
			.withSecond(0)
			.withNano(0);

		return matchParticipantRepository.findByPuuidAndMatch_GameCreationGreaterThanEqual(puuid, monday);
	}
}
