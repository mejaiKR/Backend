package mejai.mejaigg.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import mejai.mejaigg.domain.SearchHistory;
import mejai.mejaigg.domain.User;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
	Optional<SearchHistory> findByUserAndYearMonth(User user, String yearMonth);
}
