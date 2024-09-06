package mejai.mejaigg.summoner.repository;

import java.time.YearMonth;
import java.util.List;

import mejai.mejaigg.searchhistory.dto.TopUserDTO;

public interface SummonerCustom {
	List<TopUserDTO> findTop10UsersWithGameCountByMonth(YearMonth yearMonth);
}
