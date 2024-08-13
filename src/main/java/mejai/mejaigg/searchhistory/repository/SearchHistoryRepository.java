package mejai.mejaigg.searchhistory.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jakarta.transaction.Transactional;
import mejai.mejaigg.searchhistory.entity.SearchHistory;
import mejai.mejaigg.summoner.entity.User;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
	Optional<SearchHistory> findByUserAndYearMonth(User user, String yearMonth);

	@Transactional
	@Modifying
	@Query("UPDATE SearchHistory sh SET sh.isDone = :isDone WHERE sh.historyId = :historyId")
	void updateIsDoneByHistoryId(Long historyId, boolean isDone);

	@Transactional
	@Modifying
	@Query("UPDATE SearchHistory sh SET sh.lastSuccessDay = :lastSuccessDay WHERE sh.historyId = :historyId")
	void updateLastSuccessDateByHistoryId(Long historyId, int lastSuccessDay);
}
