package mejai.mejaigg.rank;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mejai.mejaigg.rank.dto.RankDto;
import mejai.mejaigg.rank.entity.Rank;
import mejai.mejaigg.rank.mapper.RankMapper;
import mejai.mejaigg.rank.repository.RankRepository;
import mejai.mejaigg.riot.service.RiotService;
import mejai.mejaigg.summoner.entity.User;

@Service
@RequiredArgsConstructor
public class RankService {

	private final RankRepository rankRepository;
	private final RiotService riotService;

	public Set<Rank> createRanks(Set<RankDto> rankDtos, User user) {
		Set<Rank> ranks = new HashSet<>();
		rankDtos.stream()
			.forEach(rankDto -> setNewRankByRankDto(rankDto, user, ranks));

		if (rankDtos.stream().noneMatch(rank -> rank.getQueueType().equals("RANKED_SOLO_5x5"))) {
			ranks.add(createUnranked(user, true));
		}
		if (rankDtos.stream().noneMatch(rank -> rank.getQueueType().equals("RANKED_FLEX_SR"))) {
			ranks.add(createUnranked(user, false));
		}
		return ranks;
	}

	public void updateUserRanks(User user) {
		Set<RankDto> rankDtos = riotService.getRankBySummonerId(user.getSummonerId());

		// 랭크 DTO로부터 랭크 객체를 업데이트
		updateRank(user, rankDtos, "RANKED_SOLO_5x5");
		updateRank(user, rankDtos, "RANKED_FLEX_SR");
	}

	private void updateRank(User user, Set<RankDto> rankDtos, String queueType) {
		RankDto rankDto = rankDtos.stream()
			.filter(rank -> rank.getQueueType().equals(queueType))
			.findFirst()
			.orElse(null);

		if (rankDto != null) {
			user.getRank().stream()
				.filter(rank -> rank.getQueueType().equals(queueType))
				.findFirst()
				.ifPresent(rank -> {
					rank.updateByRankDto(rankDto);
					rankRepository.save(rank); // 랭크를 업데이트하고 저장
				});
		}
	}

	private void setNewRankByRankDto(RankDto rankDto, User user, Set<Rank> ranks) {
		Rank rank = RankMapper.INSTANCE.toRankEntity(rankDto);
		rank.setUser(user);
		rank.setPuuid(user.getPuuid());
		ranks.add(rank);
	}

	private Rank createUnranked(User user, boolean isSolo) {
		Rank rank = new Rank();
		rank.setUnRanked(isSolo);
		rank.setUser(user);
		rank.setPuuid(user.getPuuid());
		return rank;
	}
}
