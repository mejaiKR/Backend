package mejai.mejaigg.searchhistory.service;

import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import mejai.mejaigg.searchhistory.dto.RankingResponse;
import mejai.mejaigg.searchhistory.dto.TopUserDTO;
import mejai.mejaigg.summoner.repository.SummonerRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchHistoryService {

	private final SummonerRepository summonerRepository;

	@Transactional
	public RankingResponse getRanking(int year, int month) {
		YearMonth dateYM = YearMonth.of(year, month);
		List<TopUserDTO> top10UsersWithGameCountByMonth = summonerRepository.findTop10UsersWithGameCountByMonth(dateYM);
		return new RankingResponse(top10UsersWithGameCountByMonth);
	}

}
