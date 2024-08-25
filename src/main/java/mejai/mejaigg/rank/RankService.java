package mejai.mejaigg.rank;

import java.util.Set;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mejai.mejaigg.rank.domain.Rank;
import mejai.mejaigg.rank.dto.RankDto;
import mejai.mejaigg.rank.repository.RankRepository;
import mejai.mejaigg.riot.service.RiotService;
import mejai.mejaigg.summoner.domain.Summoner;

@Service
@RequiredArgsConstructor
public class RankService {

	private final RankRepository rankRepository;
	private final RiotService riotService;

	public void updateUserRanks(Summoner user) {
		Set<RankDto> rankDtos = riotService.getRankBySummonerId(user.getSummonerId());

		// 랭크 DTO로부터 랭크 객체를 업데이트
		updateRank(user, rankDtos, "RANKED_SOLO_5x5");
		updateRank(user, rankDtos, "RANKED_FLEX_SR");
	}

	private void updateRank(Summoner user, Set<RankDto> rankDtos, String queueType) {
		RankDto rankDto = rankDtos.stream()
			.filter(rank -> rank.getQueueType().equals(queueType))
			.findFirst()
			.orElse(null);

		if (rankDto != null) {
			user.getRanks().stream()
				.filter(rank -> rank.getId().getQueueType().equals(queueType))
				.findFirst()
				.ifPresent(rank -> {
					rank.updateByRankDto(rankDto);
					rankRepository.save(rank); // 랭크를 업데이트하고 저장
				});
		}
	}

	private void setNewRankByRankDto(RankDto rankDto, Summoner user, Set<Rank> ranks) {
		//
		// rank.setUser(user);
		// rank.setId(user.getId());
		// ranks.add(rank);
	}

}
