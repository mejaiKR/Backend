package mejai.mejaigg.app.watch.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mejai.mejaigg.app.user.domain.AppUser;
import mejai.mejaigg.app.user.domain.Relationship;
import mejai.mejaigg.app.user.service.UserService;
import mejai.mejaigg.app.watch.dto.CreateSummonerResponse;
import mejai.mejaigg.app.watch.dto.SearchSummonerResponse;
import mejai.mejaigg.app.watch.dto.watch.DayLog;
import mejai.mejaigg.app.watch.dto.watch.PlayLog;
import mejai.mejaigg.app.watch.dto.watch.WatchSummoner;
import mejai.mejaigg.app.watch.dto.watch.WatchSummonerResponse;
import mejai.mejaigg.global.config.RiotProperties;
import mejai.mejaigg.match.domain.Match;
import mejai.mejaigg.match.service.MatchService;
import mejai.mejaigg.matchparticipant.domain.MatchParticipant;
import mejai.mejaigg.matchparticipant.service.MatchParticipantService;
import mejai.mejaigg.summoner.domain.Summoner;
import mejai.mejaigg.summoner.repository.SummonerRepository;
import mejai.mejaigg.summoner.service.ProfileService;

@Service
@Slf4j
@RequiredArgsConstructor
public class WatchService {
	private final SummonerRepository summonerRepository;
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
		Summoner summoner = summonerRepository.findBySummonerNameAndTagLineAllIgnoreCase(summonerName, tag)
			.orElse(null);
		if (summoner == null) {
			summoner = profileService.initializeSummonerData(summonerName, tag);
		}

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

	public SearchSummonerResponse getSummoner(String summonerName, String tag) {
		Summoner summoner = profileService.findOrCreateSummoner(summonerName, tag);

		return new SearchSummonerResponse(summoner, riotProperties.getResourceUrl());
	}

	public WatchSummonerResponse getSummonerRecord(Long userId) {
		AppUser user = userService.findUserById(userId);
		Summoner summoner = user.getWatchSummoner();
		if (summoner == null) {
			throw new IllegalArgumentException("소환사를 감시하고 있지 않습니다.");
		}
		List<MatchParticipant> matchesLog = matchParticipantService.findMatchesOneWeek(summoner.getPuuid());

		WatchSummoner summonerDto = new WatchSummoner(user, riotProperties.getResourceUrl());
		DayLog today = createDayLogDto(matchesLog);
		List<PlayLog> todayPlayLog = createTodayPlayLog(matchesLog);
		List<DayLog> thisWeek = createWeekLog(matchesLog);

		return new WatchSummonerResponse(summonerDto, today, todayPlayLog, thisWeek);
	}

	private List<PlayLog> createTodayPlayLog(List<MatchParticipant> matchesLog) {
		List<PlayLog> todayPlayLog = new ArrayList<>();

		List<MatchParticipant> todayLogs = toDayMatchParticipants(matchesLog, LocalDate.now());
		for (MatchParticipant matchParticipant : todayLogs) {
			Match match = matchParticipant.getMatch();
			LocalTime gameStartTime = match.getGameStartTimestamp().toLocalTime();
			LocalTime gameEndTime = match.getGameEndTimestamp().toLocalTime();

			todayPlayLog.add(new PlayLog(gameStartTime, gameEndTime, matchParticipant.getWin()));
		}

		return todayPlayLog;
	}

	private List<DayLog> createWeekLog(List<MatchParticipant> matchesLog) {
		List<DayLog> weekLog = new ArrayList<>();

		LocalDate today = LocalDate.now();
		LocalDate monday = today.with(DayOfWeek.MONDAY);
		for (LocalDate day = monday; !day.isAfter(today); day = day.plusDays(1)) {
			List<Match> dayMatches = toDayMatches(matchesLog, day);

			int playCount = dayMatches.size();
			long playTime = dayMatches.stream()
				.mapToLong(Match::getGameDuration)
				.sum();

			weekLog.add(new DayLog(playCount, playTime));
		}

		return weekLog;
	}

	private DayLog createDayLogDto(List<MatchParticipant> matchesLog) {
		// if game creation time is today
		LocalDate today = LocalDate.now();
		List<Match> todayMatches = toDayMatches(matchesLog, today);

		int playCount = todayMatches.size();
		long playTime = todayMatches.stream()
			.mapToLong(Match::getGameDuration)
			.sum();

		return new DayLog(playCount, playTime);
	}

	private List<Match> toDayMatches(List<MatchParticipant> matchesLog, LocalDate day) {
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
}
