package mejai.mejaigg.user.repository;

import java.util.List;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import mejai.mejaigg.matchdatestreak.entity.QMatchDateStreak;
import mejai.mejaigg.searchhistory.dto.TopUserDTO;
import mejai.mejaigg.searchhistory.entity.QSearchHistory;
import mejai.mejaigg.user.entity.QUser;

public class UserRepositoryImpl implements UserRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	public UserRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
	}

	@Override
	public List<TopUserDTO> findTop10UsersWithGameCountByMonth(String yearMonth) {
		QUser user = QUser.user;
		QSearchHistory searchHistory = QSearchHistory.searchHistory;
		QMatchDateStreak matchDateStreak = QMatchDateStreak.matchDateStreak;
		NumberExpression<Long> totalGameCount = matchDateStreak.allGameCount.sum().longValue();
		return queryFactory
			.select(Projections.constructor(TopUserDTO.class,
				user.summonerName,
				user.tagLine,
				totalGameCount))
			.from(user)
			.join(user.searchHistory, searchHistory)
			.join(searchHistory.matchDateStreaks, matchDateStreak)
			.where(searchHistory.yearMonth.eq(yearMonth))
			.groupBy(user.summonerName, user.tagLine)
			.orderBy(matchDateStreak.allGameCount.sum().desc())
			.limit(10)
			.fetch();
	}
}
