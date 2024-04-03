package mejai.mejaigg.service;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import jakarta.transaction.Transactional;
import mejai.mejaigg.common.YearMonthToEpochUtil;
import mejai.mejaigg.dto.riot.AccountDto;
import mejai.mejaigg.dto.riot.RankDto;
import mejai.mejaigg.dto.riot.SummonerDto;
import mejai.mejaigg.dto.riot.match.MatchDto;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ApiServiceTest {

	@Autowired
	ApiService apiService;

	@Test
	public void testApiFunctionality() {
		//given
		// String summonerName = "hide on bush";
		String summonerName = "김승종";
		String tag = "과로사";
		String matchId = "KR_6983879233";
		String dateYM = "2024-01";
		Long epochSeconds = YearMonthToEpochUtil.convertToEpochSeconds(dateYM);
		Long epochSeconds2 = YearMonthToEpochUtil.addMonthEpochSecond(dateYM, 1);
		// Long epochSeconds2 = YearMonthToEpochUtil.convertToEpochSeconds("2024-01");
		//when
		SummonerDto summonerDto = apiService.getSummonerByName(summonerName).block();
		System.out.println("summonerDto = " + summonerDto);
		SummonerDto summonerDto2 = apiService.getSummonerByPuuid(summonerDto.getPuuid()).block();
		System.out.println("summonerDto2 = " + summonerDto2);
		AccountDto accountDto = apiService.getAccountByNameAndTag(summonerName, tag).block();
		System.out.println("accoutDto = " + accountDto);
		Set<RankDto> rankDtos = apiService.getRankBySummonerId(summonerDto.getId()).block();
		System.out.println("rankDtos = " + rankDtos);
		String[] matchStore = apiService.getMatchHistoryByPuuid(summonerDto.getPuuid(),
			epochSeconds, epochSeconds2, 0L, 100).block();
		for (String s : matchStore) {
			System.out.println("s = " + s);
		}
		MatchDto matchDto = apiService.getMatchDtoByMatchId(matchStore[0]).block();
		System.out.println("matchDto = " + matchDto);
		//then
		assertNotNull(summonerDto);
		assertNotNull(summonerDto2);
		assertNotNull(accountDto);
		assertNotNull(rankDtos);
		assertNotNull(matchDto);
		assertEquals(summonerDto.getId(), summonerDto2.getId());
	}
}
