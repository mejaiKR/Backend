package mejai.mejaigg.searchhistory.service;

import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import mejai.mejaigg.searchhistory.dto.SearchRankingDto;
import mejai.mejaigg.searchhistory.dto.TopUserDTO;
import mejai.mejaigg.summoner.repository.SummonerRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchHistoryService {

	private final SummonerRepository userRepository;

	@Transactional
	public SearchRankingDto getRanking(int year, int month) {
		YearMonth dateYM = YearMonth.of(year, month);
		SearchRankingDto searchRankingDto = new SearchRankingDto();
		searchRankingDto.setYear(String.valueOf(year));
		searchRankingDto.setMonth(String.valueOf(month));

		List<TopUserDTO> top10UsersWithGameCountByMonth = userRepository.findTop10UsersWithGameCountByMonth(dateYM);
		searchRankingDto.setTopRanking(top10UsersWithGameCountByMonth);
		return searchRankingDto;
	}

}
