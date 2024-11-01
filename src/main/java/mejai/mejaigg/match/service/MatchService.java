package mejai.mejaigg.match.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mejai.mejaigg.match.domain.Match;
import mejai.mejaigg.match.repository.MatchRepository;
import mejai.mejaigg.match_participant.service.MatchParticipantService;
import mejai.mejaigg.riot.service.RiotService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MatchService {
	private final MatchRepository matchRepository;
	private final MatchParticipantService matchParticipantService;
	private final RiotService riotService;
	private final ObjectMapper objectMapper;

	@Transactional
	public void createMatches(String puuid) {
		List<String> requiredMatchIds = matchParticipantService.findUnsavedMatchIds(puuid);
		List<String> matches = riotService.getMatches(requiredMatchIds);

		for (String rawMatch : matches) {
			try {
				JsonNode matchNode = objectMapper.readTree(rawMatch);
				JsonNode metadataNode = matchNode.path("metadata");
				JsonNode infoNode = matchNode.path("info");
				JsonNode participantsNode = infoNode.path("participants");
				if (infoNode.isObject()) {
					((ObjectNode) infoNode).remove("participants");
				}

				Match match = Match.parseJsonData(matchNode, metadataNode, infoNode);
				Long matchId = matchRepository.save(match).getId();

				matchParticipantService.createMatchParticipants(participantsNode, matchId);
			} catch (Exception e) {
				log.error("Match parsing error : {}", e.getMessage());
			}
		}
	}
}
