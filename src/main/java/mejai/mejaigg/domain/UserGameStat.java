package mejai.mejaigg.domain;

import jakarta.persistence.*;
import lombok.Getter;
import mejai.mejaigg.dto.riot.match.MatchDto;
import mejai.mejaigg.dto.riot.match.ParticipantDto;

@Entity
@Getter
public class UserGameStat {
	@Id
	@GeneratedValue
	@Column(name = "user_game_stat_id")
	private Long id;

	private String puuid; // 이거 그냥 user puuid 랑 같습니다.

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "matchId")
	private Game game;

	private int assists;
	private int baronKills;
	private int bountyLevel;
	private int champExperience;
	private int champLevel;
	private int championId;
	private String championName;
	private int championTransform;
	private int consumablesPurchased;
	private int damageDealtToBuildings;
	private int damageDealtToObjectives;
	private int damageDealtToTurrets;
	private int damageSelfMitigated;
	private int deaths;
	private int detectorWardsPlaced;
	private int doubleKills;
	private int dragonKills;
	private boolean firstBloodAssist;
	private boolean firstBloodKill;
	private boolean firstTowerAssist;
	private boolean firstTowerKill;
	private boolean gameEndedInEarlySurrender;
	private boolean gameEndedInSurrender;
	private int goldEarned;
	private int goldSpent;
	private String individualPosition;
	private int inhibitorKills;
	private int inhibitorTakedowns;
	private int inhibitorsLost;

	private int item0;
	private int item1;
	private int item2;
	private int item3;
	private int item4;
	private int item5;
	private int item6;

	private int itemsPurchased;
	private int killingSprees;
	private int kills;
	private String lane;
	private int largestCriticalStrike;
	private int largestKillingSpree;
	private int largestMultiKill;
	private int longestTimeSpentLiving;
	private int magicDamageDealt;
	private int magicDamageDealtToChampions;
	private int magicDamageTaken;
	private int neutralMinionsKilled;
	private int nexusKills;
	private int nexusLost;
	private int nexusTakedowns;
	private int objectivesStolen;
	private int objectivesStolenAssists;
	private int participantId;
	private int pentaKills;

	private int physicalDamageDealt;
	private int physicalDamageDealtToChampions;
	private int physicalDamageTaken;
	private int profileIcon;

	private int quadraKills;
	private String riotIdName;
	private String riotIdTagline;
	private String role;
	private int sightWardsBoughtInGame;
	private int spell1Casts;
	private int spell2Casts;
	private int spell3Casts;
	private int spell4Casts;
	private int summoner1Casts;
	private int summoner1Id;
	private int summoner2Casts;
	private int summoner2Id;
	private String summonerId;
	private int summonerLevel;
	private String summonerName;
	private boolean teamEarlySurrendered;
	private int teamId;
	private String teamPosition;
	private int timeCCingOthers;
	private int timePlayed;
	private int totalDamageDealt;
	private int totalDamageDealtToChampions;
	private int totalDamageShieldedOnTeammates;
	private int totalDamageTaken;
	private int totalHeal;
	private int totalHealsOnTeammates;
	private int totalMinionsKilled;
	private int totalTimeCCDealt;
	private int totalTimeSpentDead;
	private int totalUnitsHealed;
	private int tripleKills;
	private int trueDamageDealt;
	private int trueDamageDealtToChampions;
	private int trueDamageTaken;
	private int turretKills;
	private int turretTakedowns;
	private int turretsLost;
	private int unrealKills;
	private int visionScore;
	private int visionWardsBoughtInGame;
	private int wardsKilled;
	private int wardsPlaced;
	private boolean win;

	public void setByParticipantDto(ParticipantDto participantDto) {
		puuid = participantDto.getPuuid();
		assists = participantDto.getAssists();
		baronKills = participantDto.getBaronKills();
		bountyLevel = participantDto.getBountyLevel();
		champExperience = participantDto.getChampExperience();
		champLevel = participantDto.getChampLevel();
		championId = participantDto.getChampionId();
		championName = participantDto.getChampionName();
		championTransform = participantDto.getChampionTransform();
		consumablesPurchased = participantDto.getConsumablesPurchased();
		damageDealtToBuildings = participantDto.getDamageDealtToBuildings();
		damageDealtToObjectives = participantDto.getDamageDealtToObjectives();
		damageDealtToTurrets = participantDto.getDamageDealtToTurrets();
		damageSelfMitigated = participantDto.getDamageSelfMitigated();
		deaths = participantDto.getDeaths();
		detectorWardsPlaced = participantDto.getDetectorWardsPlaced();
		doubleKills = participantDto.getDoubleKills();
		dragonKills = participantDto.getDragonKills();
		firstBloodAssist = participantDto.isFirstBloodAssist();
		firstBloodKill = participantDto.isFirstBloodKill();
		firstTowerAssist = participantDto.isFirstTowerAssist();
		firstTowerKill = participantDto.isFirstTowerKill();
		gameEndedInEarlySurrender = participantDto.isGameEndedInEarlySurrender();
		gameEndedInSurrender = participantDto.isGameEndedInSurrender();
		goldEarned = participantDto.getGoldEarned();
		goldSpent = participantDto.getGoldSpent();
		individualPosition = participantDto.getIndividualPosition();
		inhibitorKills = participantDto.getInhibitorKills();
		inhibitorTakedowns = participantDto.getInhibitorTakedowns();
		inhibitorsLost = participantDto.getInhibitorsLost();
		item0 = participantDto.getItem0();
		item1 = participantDto.getItem1();
		item2 = participantDto.getItem2();
		item3 = participantDto.getItem3();
		item4 = participantDto.getItem4();
		item5 = participantDto.getItem5();
		item6 = participantDto.getItem6();
		itemsPurchased = participantDto.getItemsPurchased();
		killingSprees = participantDto.getKillingSprees();
		kills = participantDto.getKills();
		lane = participantDto.getLane();
		largestCriticalStrike = participantDto.getLargestCriticalStrike();
		largestKillingSpree = participantDto.getLargestKillingSpree();
		largestMultiKill = participantDto.getLargestMultiKill();
		longestTimeSpentLiving = participantDto.getLongestTimeSpentLiving();
		magicDamageDealt = participantDto.getMagicDamageDealt();
		magicDamageDealtToChampions = participantDto.getMagicDamageDealtToChampions();
		magicDamageTaken = participantDto.getMagicDamageTaken();
		neutralMinionsKilled = participantDto.getNeutralMinionsKilled();
		nexusKills = participantDto.getNexusKills();
		nexusLost = participantDto.getNexusLost();
		nexusTakedowns = participantDto.getNexusTakedowns();
		objectivesStolen = participantDto.getObjectivesStolen();
		objectivesStolenAssists = participantDto.getObjectivesStolenAssists();
		pentaKills = participantDto.getPentaKills();
		physicalDamageDealt = participantDto.getPhysicalDamageDealt();
		physicalDamageDealtToChampions = participantDto.getPhysicalDamageDealtToChampions();
		physicalDamageTaken = participantDto.getPhysicalDamageTaken();
		profileIcon = participantDto.getProfileIcon();
		quadraKills = participantDto.getQuadraKills();
		riotIdName = participantDto.getRiotIdName();
		riotIdTagline = participantDto.getRiotIdTagline();
		role = participantDto.getRole();
		sightWardsBoughtInGame = participantDto.getSightWardsBoughtInGame();
		spell1Casts = participantDto.getSpell1Casts();
		spell2Casts = participantDto.getSpell2Casts();
		spell3Casts = participantDto.getSpell3Casts();
		spell4Casts = participantDto.getSpell4Casts();
		summoner1Casts = participantDto.getSummoner1Casts();
		summoner1Id = participantDto.getSummoner1Id();
		summoner2Casts = participantDto.getSummoner2Casts();
		summoner2Id = participantDto.getSummoner2Id();
		summonerId = participantDto.getSummonerId();
		summonerLevel = participantDto.getSummonerLevel();
		summonerName = participantDto.getSummonerName();
		teamEarlySurrendered = participantDto.isTeamEarlySurrendered();
		teamId = participantDto.getTeamId();
		teamPosition = participantDto.getTeamPosition();
		timeCCingOthers = participantDto.getTimeCCingOthers();
		timePlayed = participantDto.getTimePlayed();

		totalDamageDealt = participantDto.getTotalDamageDealt();
		totalDamageDealtToChampions = participantDto.getTotalDamageDealtToChampions();
		totalDamageShieldedOnTeammates = participantDto.getTotalDamageShieldedOnTeammates();
		totalDamageTaken = participantDto.getTotalDamageTaken();
		totalHeal = participantDto.getTotalHeal();
		totalHealsOnTeammates = participantDto.getTotalHealsOnTeammates();
		totalMinionsKilled = participantDto.getTotalMinionsKilled();
		totalTimeCCDealt = participantDto.getTotalTimeCCDealt();
		totalTimeSpentDead = participantDto.getTotalTimeSpentDead();
		totalUnitsHealed = participantDto.getTotalUnitsHealed();
		tripleKills = participantDto.getTripleKills();
		trueDamageDealt = participantDto.getTrueDamageDealt();
		trueDamageDealtToChampions = participantDto.getTrueDamageDealtToChampions();
		trueDamageTaken = participantDto.getTrueDamageTaken();
		turretKills = participantDto.getTurretKills();
		turretTakedowns = participantDto.getTurretTakedowns();
		turretsLost = participantDto.getTurretsLost();
		unrealKills = participantDto.getUnrealKills();
		visionScore = participantDto.getVisionScore();
		visionWardsBoughtInGame = participantDto.getVisionWardsBoughtInGame();
		wardsKilled = participantDto.getWardsKilled();
		wardsPlaced = participantDto.getWardsPlaced();
		win = participantDto.isWin();
	}

	public void setGame(Game game) {
		this.game = game;
	}
//	public void setMatchParticipant(MatchParticipant matchParticipant) {
//		this.matchParticipant = matchParticipant;
//	}
}
