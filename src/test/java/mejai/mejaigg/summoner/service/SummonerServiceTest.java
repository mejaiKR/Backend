package mejai.mejaigg.summoner.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import mejai.mejaigg.global.config.RiotProperties;
import mejai.mejaigg.rank.domain.TierType;
import mejai.mejaigg.rank.dto.RankDto;
import mejai.mejaigg.rank.repository.RankRepository;
import mejai.mejaigg.riot.dto.AccountDto;
import mejai.mejaigg.riot.dto.SummonerDto;
import mejai.mejaigg.riot.service.RiotService;
import mejai.mejaigg.summoner.domain.Summoner;
import mejai.mejaigg.summoner.dto.response.SummonerProfileResponse;
import mejai.mejaigg.summoner.repository.SummonerRepository;

@SpringBootTest
@DisplayName("소환사 프로필 서비스 테스트")
@Transactional
class SummonerServiceTest {

	@MockBean
	private RiotService riotService;

	@MockBean
	private RiotProperties riotProperties;

	@Autowired
	private SummonerService summonerService;

	@Autowired
	SummonerRepository summonerRepository;

	@Autowired
	RankRepository rankRepository;

	@BeforeEach
	public void setUp() {
		when(riotProperties.getResourceUrl()).thenReturn("http://test.url");
	}

	@Test
	@DisplayName("UNRANKED 인 유저의 정보가 이미 DB에 있는 경우 가져올 수 있다.")
	public void testInitializeSummoner() {
		// given
		String name = "testName";
		String tag = "testTag";
		Summoner summoner = Summoner.builder()
			.summonerName(name)
			.tagLine(tag)
			.puuid("testPuuid")
			.accountId("testAccountId")
			.summonerId("testSummonerId")
			.revisionDate(123456789L)
			.profileIconId(123)
			.summonerLevel(10L)
			.build();
		summonerRepository.save(summoner);
		summoner.setRankByRankDtos(new HashSet<>());
		summonerRepository.save(summoner);
		// when
		SummonerProfileResponse summonerProfile = summonerService.getSummonerProfileByNameTag(name, tag);
		// then
		assertNotNull(summonerProfile);
		assertNotNull(summonerProfile.getSoloRank());
		assertNotNull(summonerProfile.getFlexRank());
		assertEquals(name, summonerProfile.getSummonerName());
		assertEquals(tag, summonerProfile.getTagLine());
		assertEquals(TierType.UNRANKED, summonerProfile.getSoloRank().getTier());
		assertEquals(TierType.UNRANKED, summonerProfile.getFlexRank().getTier());
	}

	@Test
	@DisplayName("처음 요청을 하고 유저가 실제로 있는 경우")
	public void testRefreshSummonerProfile() {
		// given
		String name = "refreshName";
		String tag = "refreshTag";
		Summoner existingSummoner = Summoner.builder()
			.summonerName(name)
			.tagLine(tag)
			.puuid("refreshPuuid")
			.accountId("refreshAccountId")
			.summonerId("refreshSummonerId")
			.revisionDate(123456789L)
			.profileIconId(123)
			.summonerLevel(10L)
			.build();
		Set<RankDto> rankDtos = new HashSet<>();
		when(riotService.getAccountByNameAndTag(name, tag)).thenReturn(
			AccountDto.builder()
				.gameName(existingSummoner.getSummonerName())
				.tagLine(existingSummoner.getTagLine())
				.puuid(existingSummoner.getPuuid()).build());
		when(riotService.getSummonerByPuuid(eq(existingSummoner.getPuuid()))).thenReturn(
			SummonerDto.builder()
				.id(existingSummoner.getSummonerId())
				.accountId(existingSummoner.getAccountId())
				.puuid(existingSummoner.getPuuid())
				.summonerLevel(existingSummoner.getSummonerLevel())
				.profileIconId(existingSummoner.getProfileIconId())
				.revisionDate(existingSummoner.getRevisionDate())
				.build());
		when(riotService.getRankBySummonerId(eq(existingSummoner.getSummonerId()))).thenReturn(rankDtos);
		//when
		existingSummoner.setUpdatedAt(LocalDateTime.now().minusHours(3));
		SummonerDto summonerDto = SummonerDto.builder()
			.id("refreshedId")
			.accountId("refreshedAccountId")
			.puuid("refreshPuuid")
			.summonerLevel(10L)
			.profileIconId(123)
			.revisionDate(123456789L)
			.build();

		// when
		SummonerProfileResponse summonerProfile = summonerService.getSummonerProfileByNameTag(name, tag);
		// then
		assertNotNull(summonerProfile);
		assertNotNull(summonerProfile.getSoloRank());
		assertNotNull(summonerProfile.getFlexRank());
		assertEquals(name, summonerProfile.getSummonerName());
		assertEquals(tag, summonerProfile.getTagLine());
		assertEquals(TierType.UNRANKED, summonerProfile.getSoloRank().getTier());
		assertEquals(TierType.UNRANKED, summonerProfile.getFlexRank().getTier());

	}

	@Test
	@DisplayName("2시간 이내 갱신시 라이엇으로 요청을 보내지 않는다.")
	public void testNoForcedUpdateWithinTwoHours() {
		// given
		String name = "noUpdateName";
		String tag = "noUpdateTag";
		Summoner existingSummoner = Summoner.builder()
			.summonerName(name)
			.tagLine(tag)
			.puuid("noUpdatePuuid")
			.revisionDate(123456789L)
			.profileIconId(123)
			.summonerLevel(10L)
			.summonerId("noUpdateSummonerId")
			.accountId("noUpdateAccountId")
			.build();
		existingSummoner.setUpdatedAt(LocalDateTime.now().minusMinutes(30));
		existingSummoner.setRankByRankDtos(new HashSet<>());
		summonerRepository.save(existingSummoner);
		// when
		SummonerProfileResponse summonerProfileResponse = summonerService.renewalSummonerProfileByNameTag(name, tag);

		// then

		verify(riotService, never()).getSummonerByPuuid(anyString());

		assertNotNull(summonerProfileResponse);
		assertEquals(name, summonerProfileResponse.getSummonerName());
		assertEquals(tag, summonerProfileResponse.getTagLine());
	}

	@DisplayName("2시간 이후 갱신시 라이엇으로 요청을 보낸다.")
	@Test
	public void testForcedUpdateAfterTwoHours() {
		// given
		String name = "forcedUpdateName";
		String tag = "forcedUpdateTag";
		Summoner existingSummoner = Summoner.builder()
			.summonerName(name)
			.tagLine(tag)
			.puuid("forcedUpdatePuuid")
			.revisionDate(123456789L)
			.profileIconId(123)
			.summonerLevel(10L)
			.summonerId("forcedUpdateSummonerId")
			.accountId("forcedUpdateAccountId")
			.build();
		existingSummoner.setUpdatedAt(LocalDateTime.now().minusHours(3));
		existingSummoner.setRankByRankDtos(new HashSet<>());
		summonerRepository.save(existingSummoner);
		when(riotService.getSummonerByPuuid(anyString())).thenReturn(
			SummonerDto.builder()
				.id("forcedUpdateSummonerId")
				.accountId("forcedUpdateAccountId")
				.puuid("forcedUpdatePuuid")
				.summonerLevel(10L)
				.profileIconId(123)
				.revisionDate(123456789L)
				.build());
		when(riotService.getRankBySummonerId(anyString())).thenReturn(new HashSet<>());
		// when
		SummonerProfileResponse summonerProfileResponse = summonerService.renewalSummonerProfileByNameTag(name, tag);
		// then
		assertNotNull(summonerProfileResponse);
		assertEquals(name, summonerProfileResponse.getSummonerName());
		assertEquals(tag, summonerProfileResponse.getTagLine());
	}
}
