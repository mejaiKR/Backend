package kr.mejai.project.domain.summoner.repository;

import java.time.YearMonth;
import java.util.List;

import kr.mejai.project.domain.searchhistory.dto.TopUserDTO;

public interface SummonerCustom {
	List<TopUserDTO> findTop10UsersWithGameCountByMonth(YearMonth yearMonth);
}
