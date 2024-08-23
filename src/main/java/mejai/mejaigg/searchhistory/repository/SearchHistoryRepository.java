package mejai.mejaigg.searchhistory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jakarta.transaction.Transactional;
import mejai.mejaigg.searchhistory.domain.SearchHistory;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
	// Optional<SearchHistory> findByUserAndYearMonth(User user, YearMonth yearMonth);

	@Transactional
	@Modifying
	@Query("UPDATE SearchHistory sh SET sh.done = :isDone WHERE sh.id = :historyId")
	void updateIsDoneByHistoryId(Long historyId, boolean isDone);

	@Transactional
	@Modifying
	@Query("UPDATE SearchHistory sh SET sh.lastSuccessDay = :lastSuccessDay WHERE sh.id = :historyId")
	void updateLastSuccessDateByHistoryId(Long historyId, int lastSuccessDay);
}
