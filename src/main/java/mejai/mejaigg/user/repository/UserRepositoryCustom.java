package mejai.mejaigg.user.repository;

import java.util.List;

import mejai.mejaigg.searchhistory.dto.TopUserDTO;

public interface UserRepositoryCustom {
	List<TopUserDTO> findTop10UsersWithGameCountByMonth(String yearMonth);
}
