package mejai.mejaigg.watch.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mejai.mejaigg.global.config.RiotProperties;
import mejai.mejaigg.match.domain.Match;
import mejai.mejaigg.match.service.MatchService;
import mejai.mejaigg.match_participant.domain.MatchParticipant;
import mejai.mejaigg.match_participant.service.MatchParticipantService;
import mejai.mejaigg.summoner.domain.Summoner;
import mejai.mejaigg.summoner.repository.SummonerRepository;
import mejai.mejaigg.summoner.service.ProfileService;
import mejai.mejaigg.watch.dto.PostWatchSummonerDto;
import mejai.mejaigg.watch.dto.watch_summoner.DayLogDto;
import mejai.mejaigg.watch.dto.watch_summoner.GetWatchSummonerDto;
import mejai.mejaigg.watch.dto.watch_summoner.PlayLogDto;
import mejai.mejaigg.watch.dto.watch_summoner.SummonerDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class WatchService {
	private final SummonerRepository summonerRepository;
	private final ProfileService profileService;
	private final MatchService matchService;
	private final MatchParticipantService matchParticipantService;
	private final RiotProperties riotProperties;

	@Transactional
	public PostWatchSummonerDto watchSummoner(String summonerName, String tag) {
		Summoner summoner = summonerRepository.findBySummonerNameAndTagLineAllIgnoreCase(summonerName, tag).orElse(null);
		if (summoner == null) {
			summoner = profileService.initializeSummonerData(summonerName, tag);
		}

		matchService.createMatches(summoner.getPuuid());

		return new PostWatchSummonerDto(summoner.getId());
	}

	public SummonerDto getSummoner(String summonerName, String tag) {
		Summoner summoner = profileService.findOrCreateSummoner(summonerName, tag);

		return new SummonerDto(summoner, riotProperties.getResourceUrl());
	}

	public GetWatchSummonerDto getSummonerRecord(String summonerName, String tag) {
		Summoner summoner = profileService.findOrCreateSummoner(summonerName, tag);
		List<MatchParticipant> matchesLog = matchParticipantService.findMatchesOneWeek(summoner.getPuuid());

		SummonerDto summonerDto = new SummonerDto(summoner, riotProperties.getResourceUrl());
		DayLogDto today = createDayLogDto(matchesLog);
		List<PlayLogDto> todayPlayLog = createTodayPlayLog(matchesLog);
		List<DayLogDto> thisWeek = createWeekLog(matchesLog);

		return new GetWatchSummonerDto(summonerDto, today, todayPlayLog, thisWeek);
	}

	private List<PlayLogDto> createTodayPlayLog(List<MatchParticipant> matchesLog) {
		List<PlayLogDto> todayPlayLog = new ArrayList<>();

		List<MatchParticipant> todayLogs = toDayMatchParticipants(matchesLog, LocalDate.now());
		for (MatchParticipant matchParticipant : todayLogs) {
			Match match = matchParticipant.getMatch();
			LocalTime gameStartTime = match.getGameStartTimestamp().toLocalTime();
			LocalTime gameEndTime = match.getGameEndTimestamp().toLocalTime();

			todayPlayLog.add(new PlayLogDto(gameStartTime, gameEndTime, matchParticipant.getWin()));
		}

		return todayPlayLog;
	}

	private List<DayLogDto> createWeekLog(List<MatchParticipant> matchesLog) {
		List<DayLogDto> weekLog = new ArrayList<>();

		LocalDate today = LocalDate.now();
		LocalDate monday = today.with(DayOfWeek.MONDAY);
		for (LocalDate day = monday; !day.isAfter(today); day = day.plusDays(1)) {
			List<Match> dayMatches = toDayMatches(matchesLog, day);

			int playCount = dayMatches.size();
			long playTime = dayMatches.stream()
				.mapToLong(Match::getGameDuration)
				.sum();

			weekLog.add(new DayLogDto(playCount, playTime));
		}

		return weekLog;
	}

	private DayLogDto createDayLogDto(List<MatchParticipant> matchesLog) {
		// if game creation time is today
		LocalDate today = LocalDate.now();
		List<Match> todayMatches = toDayMatches(matchesLog, today);

		int playCount = todayMatches.size();
		long playTime = todayMatches.stream()
			.mapToLong(Match::getGameDuration)
			.sum();

		return new DayLogDto(playCount, playTime);
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
