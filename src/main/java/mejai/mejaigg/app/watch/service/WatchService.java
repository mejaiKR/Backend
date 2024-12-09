package mejai.mejaigg.app.watch.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mejai.mejaigg.app.user.domain.AppUser;
import mejai.mejaigg.app.user.domain.Relationship;
import mejai.mejaigg.app.user.service.UserService;
import mejai.mejaigg.app.watch.dto.response.CreateSummonerResponse;
import mejai.mejaigg.app.watch.dto.response.RefreshWatchSummonerResponse;
import mejai.mejaigg.app.watch.dto.response.SearchSummonerResponse;
import mejai.mejaigg.app.watch.dto.response.WatchSummonerDetailsResponse;
import mejai.mejaigg.app.watch.dto.response.watch.DayLog;
import mejai.mejaigg.app.watch.dto.response.watch.PlayLog;
import mejai.mejaigg.app.watch.dto.response.watch.WatchSummoner;
import mejai.mejaigg.app.watch.dto.response.watch.WatchSummonerResponse;
import mejai.mejaigg.global.config.RiotProperties;
import mejai.mejaigg.match.domain.Match;
import mejai.mejaigg.match.service.MatchService;
import mejai.mejaigg.matchparticipant.domain.MatchParticipant;
import mejai.mejaigg.matchparticipant.service.MatchParticipantService;
import mejai.mejaigg.summoner.domain.Summoner;
import mejai.mejaigg.summoner.service.ProfileService;

@Service
@Slf4j
@RequiredArgsConstructor
public class WatchService {
	private final ProfileService profileService;
	private final MatchService matchService;
	private final MatchParticipantService matchParticipantService;
	private final RiotProperties riotProperties;
	private final UserService userService;

	@Transactional
	public CreateSummonerResponse watchSummoner(long userId,
		String summonerName,
		String tag,
		Relationship relationship) {
		Summoner summoner = profileService.findOrCreateSummoner(summonerName, tag);

		matchService.createMatches(summoner.getPuuid());

		userService.addWatchSummoner(userId, summoner, relationship);

		return new CreateSummonerResponse(summoner.getId());
	}

	@Scheduled(cron = "0 0 0/1 * * *")
	@Transactional
	public void renewal() {
		List<Summoner> watchSummoners = userService.findAllWatchSummoner();
		for (Summoner summoner : watchSummoners) {
			matchService.createMatches(summoner.getPuuid());
		}
	}

	@Transactional
	public RefreshWatchSummonerResponse refreshWatchSummoner(Long userId) {
		AppUser user = userService.findUserById(userId);
		Summoner summoner = user.getWatchSummoner();
		if (summoner == null) {
			throw new IllegalArgumentException("소환사를 감시하고 있지 않습니다.");
		}
		if (!user.canRefreshWatchSummoner()) {
			throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "소환사 감시 갱신은 1시간에 한 번만 가능합니다.");
		}

		matchService.createMatches(summoner.getPuuid());
		user.refreshWatchSummoner();

		return new RefreshWatchSummonerResponse(user.getLastUpdatedWatchSummoner());
	}

	public SearchSummonerResponse getSummoner(String summonerName, String tag) {
		Summoner summoner = profileService.findOrCreateSummoner(summonerName, tag);

		return new SearchSummonerResponse(summoner, riotProperties.getResourceUrl());
	}

	public WatchSummonerResponse getSummonerRecord(Long userId, LocalDate startDate) {
		AppUser user = userService.findUserById(userId);
		Summoner summoner = user.getWatchSummoner();
		if (summoner == null) {
			throw new IllegalArgumentException("소환사를 감시하고 있지 않습니다.");
		}
		List<MatchParticipant> matchesLog = matchParticipantService.findMatchesOneWeek(summoner.getPuuid(), startDate);

		WatchSummoner summonerDto = new WatchSummoner(user, riotProperties.getResourceUrl());
		DayLog today = createDayLogDto(matchesLog, startDate);
		List<PlayLog> dayPlayLog = createTodayPlayLog(matchesLog, startDate);
		List<DayLog> weekLog = createWeekLog(matchesLog, startDate);

		return new WatchSummonerResponse(summonerDto, today, dayPlayLog, weekLog);
	}

	public WatchSummonerDetailsResponse getSummonerRecordDetail(Long userId, LocalDate startDate) {
		AppUser user = userService.findUserById(userId);
		Summoner summoner = user.getWatchSummoner();
		if (summoner == null) {
			throw new IllegalArgumentException("소환사를 감시하고 있지 않습니다.");
		}

		List<MatchParticipant> matchesLog = matchParticipantService.findMatchesOneWeek(summoner.getPuuid(), startDate);

		LocalDate monday = startDate.with(DayOfWeek.MONDAY);
		LocalDate sunday = startDate.with(DayOfWeek.SUNDAY);
		List<List<PlayLog>> weekPlayLog = new ArrayList<>();

		for (LocalDate day = monday; !day.isAfter(sunday); day = day.plusDays(1)) {
			weekPlayLog.add(createTodayPlayLog(matchesLog, day));
		}

		return new WatchSummonerDetailsResponse(weekPlayLog);
	}

	private List<PlayLog> createTodayPlayLog(List<MatchParticipant> matchesLog, LocalDate day) {
		List<PlayLog> dayPlayLog = new ArrayList<>();

		List<MatchParticipant> todayLogs = toDayMatchParticipants(matchesLog, day);
		for (MatchParticipant matchParticipant : todayLogs) {
			Match match = matchParticipant.getMatch();
			LocalTime gameStartTime = match.getGameStartTimestamp().toLocalTime();
			LocalTime gameEndTime = match.getGameEndTimestamp().toLocalTime();

			dayPlayLog.add(new PlayLog(gameStartTime, gameEndTime, matchParticipant.getWin()));
		}

		return dayPlayLog;
	}

	private List<DayLog> createWeekLog(List<MatchParticipant> matchesLog, LocalDate startDate) {
		List<DayLog> weekLog = new ArrayList<>();

		LocalDate monday = startDate.with(DayOfWeek.MONDAY);
		LocalDate sunday = startDate.with(DayOfWeek.SUNDAY);
		for (LocalDate day = monday; !day.isAfter(sunday); day = day.plusDays(1)) {
			List<Match> dayMatches = dayMatches(matchesLog, day);

			int playCount = dayMatches.size();
			long playTime = dayMatches.stream()
				.mapToLong(Match::getGameDuration)
				.sum();

			weekLog.add(new DayLog(playCount, playTime));
		}

		return weekLog;
	}

	private DayLog createDayLogDto(List<MatchParticipant> matchesLog, LocalDate day) {
		List<Match> dayMatches = dayMatches(matchesLog, day);

		int playCount = dayMatches.size();
		long playTime = dayMatches.stream()
			.mapToLong(Match::getGameDuration)
			.sum();

		return new DayLog(playCount, playTime);
	}

	private List<Match> dayMatches(List<MatchParticipant> matchesLog, LocalDate day) {
		return matchesLog.stream()
			.map(MatchParticipant::getMatch)
			.filter(match -> match.getGameCreation().toLocalDate().isEqual(day))
			.toList();
	}

	private List<MatchParticipant> toDayMatchParticipants(List<MatchParticipant> matchesLog, LocalDate day) {
		return matchesLog.stream()
			.filter(matchParticipant -> matchParticipant.getMatch().getGameCreation().toLocalDate().isEqual(day))
			.toList();
	}

	private List<MatchParticipant> weekMatchParticipants(List<MatchParticipant> matchesLog, LocalDate startDate,
		LocalDate endDate) {
		return matchesLog.stream()
			.filter(matchParticipant -> {
				LocalDate gameCreation = matchParticipant.getMatch().getGameCreation().toLocalDate();
				return !gameCreation.isBefore(startDate) && !gameCreation.isAfter(endDate);
			})
			.toList();
	}
}
