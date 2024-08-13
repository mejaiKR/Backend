package mejai.mejaigg.searchhistory.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import mejai.mejaigg.searchhistory.dto.SearchRankingDto;
import mejai.mejaigg.searchhistory.dto.TopUserDTO;
import mejai.mejaigg.summoner.repository.UserRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchHistoryService {

	private final UserRepository userRepository;

	@Transactional
	public SearchRankingDto getRanking(int year, int month) {
		String dateYM = String.format("%d-%02d", year, month);
		SearchRankingDto searchRankingDto = new SearchRankingDto();
		searchRankingDto.setYear(String.valueOf(year));
		searchRankingDto.setMonth(String.valueOf(month));

		List<TopUserDTO> top10UsersWithGameCountByMonth = userRepository.findTop10UsersWithGameCountByMonth(dateYM);
		searchRankingDto.setTopRanking(top10UsersWithGameCountByMonth);
		return searchRankingDto;
	}

}
