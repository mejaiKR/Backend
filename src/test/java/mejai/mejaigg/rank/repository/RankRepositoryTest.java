package mejai.mejaigg.rank.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import mejai.mejaigg.global.util.TestUtil;
import mejai.mejaigg.rank.domain.Rank;
import mejai.mejaigg.rank.domain.RankId;
import mejai.mejaigg.rank.domain.TierType;
import mejai.mejaigg.summoner.domain.Summoner;
import mejai.mejaigg.summoner.repository.SummonerRepository;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class RankRepositoryTest {

	@Autowired
	private SummonerRepository summonerRepository;

	@Autowired
	private RankRepository rankRepository;

	@BeforeEach
	void setUp() {
		Summoner testSummoner = TestUtil.createTestUser();
		summonerRepository.saveAndFlush(testSummoner);
	}

	@Test
	@DisplayName("랭크 생성 및 UnRank 값이 잘 들어갔다.")
	void createRank() {
		// given
		Summoner summoner = summonerRepository.findAll().get(0);
		Rank rank = Rank.builder()
			.id(new RankId(summoner.getId(), "RANKED_SOLO_5x5"))
			.build();
		summoner.setRanks(List.of(rank));
		rankRepository.save(rank);
		// when
		Rank savedRank = rankRepository.findById(new RankId(summoner.getId(), "RANKED_SOLO_5x5"))
			.orElseThrow(() -> new AssertionError("랭크를 찾을 수 없습니다."));

		// then
		assertThat(savedRank).isNotNull();
		assertThat(savedRank.getId()).isEqualTo(new RankId(summoner.getId(), "RANKED_SOLO_5x5"));
		assertThat(savedRank.getTier()).isEqualTo(TierType.UNRANKED);
		assertThat(savedRank.getId().getQueueType()).isEqualTo(summoner.getRanks().get(0).getId().getQueueType());
	}
}
