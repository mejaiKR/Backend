package mejai.mejaigg.summoner.repository;

import java.time.YearMonth;
import java.util.List;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import mejai.mejaigg.matchstreak.domain.QMatchStreak;
import mejai.mejaigg.searchhistory.domain.QSearchHistory;
import mejai.mejaigg.searchhistory.dto.TopUserDTO;
import mejai.mejaigg.summoner.domain.QSummoner;

public class SummonerRepositoryImpl implements SummonerCustom {
	private final JPAQueryFactory queryFactory;

	public SummonerRepositoryImpl(EntityManager entityManager) {
		this.queryFactory = new JPAQueryFactory(entityManager);
	}

	@Override
	public List<TopUserDTO> findTop10UsersWithGameCountByMonth(YearMonth yearMonth) {
		QSummoner summoner = QSummoner.summoner;
		QSearchHistory searchHistory = QSearchHistory.searchHistory;
		QMatchStreak matchDateStreak = QMatchStreak.matchStreak;
		NumberExpression<Long> totalGameCount = matchDateStreak.allGameCount.sum().longValue();
		return queryFactory
			.select(Projections.constructor(TopUserDTO.class,
				summoner.summonerName,
				summoner.tagLine,
				totalGameCount))
			.from(summoner)
			.join(summoner.searchHistory, searchHistory)
			.join(searchHistory.matchStreaks, matchDateStreak)
			.where(searchHistory.date.eq(yearMonth))
			.groupBy(summoner.summonerName, summoner.tagLine)
			.orderBy(matchDateStreak.allGameCount.sum().desc())
			.limit(10)
			.fetch();
	}
}
