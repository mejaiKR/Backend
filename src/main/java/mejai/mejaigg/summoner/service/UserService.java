package mejai.mejaigg.summoner.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mejai.mejaigg.rank.dto.RankDto;
import mejai.mejaigg.rank.entity.Rank;
import mejai.mejaigg.rank.mapper.RankMapper;
import mejai.mejaigg.rank.repository.RankRepository;
import mejai.mejaigg.riot.dto.AccountDto;
import mejai.mejaigg.riot.dto.SummonerDto;
import mejai.mejaigg.riot.service.RiotService;
import mejai.mejaigg.summoner.dto.response.UserProfileDto;
import mejai.mejaigg.summoner.entity.User;
import mejai.mejaigg.summoner.mapper.UserMapper;
import mejai.mejaigg.summoner.repository.UserRepository;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

	private final RiotService riotService;
	private final UserRepository userRepository;
	private final RankRepository rankRepository;

	@Value("${variables.resourceURL:https://ddragon.leagueoflegends.com/cdn/11.16.1/img/profileicon/}")
	private String resourceURL;

	@Transactional(readOnly = false)
	public String setUserProfile(String name, String tag) { //처음 콜 할 때 세팅 되는 함수
		AccountDto accountDto = riotService.getAccountByNameAndTag(name, tag);
		SummonerDto summonerDto = riotService.getSummonerByPuuid(accountDto.getPuuid());

		User user = UserMapper.INSTANCE.toUserEntity(accountDto, summonerDto);

		Set<RankDto> rankDtos = riotService.getRankBySummonerId(summonerDto.getId());
		Set<Rank> ranks = new HashSet<>();
		RankDto soloRankDto = rankDtos.stream()
			.filter(rank -> rank.getQueueType().equals("RANKED_SOLO_5x5"))
			.findFirst()
			.orElse(null);
		RankDto flexRankDto = rankDtos.stream()
			.filter(rank -> rank.getQueueType().equals("RANKED_FLEX_SR"))
			.findFirst()
			.orElse(null);
		setNewRankByRankDto(soloRankDto, user, ranks, true);
		setNewRankByRankDto(flexRankDto, user, ranks, false);

		user.setRank(ranks);
		userRepository.save(user);

		return user.getPuuid();
	}

	private void setNewRankByRankDto(RankDto rankDto, User user, Set<Rank> ranks, boolean isSolo) {
		if (rankDto != null) {
			Rank rank = RankMapper.INSTANCE.toRankEntity(rankDto);
			rank.setUser(user);
			rank.setPuuid(user.getPuuid());
			ranks.add(rank);
		} else {
			Rank rank = new Rank();
			rank.setUnRanked(isSolo);
			rank.setUser(user);
			rank.setPuuid(user.getPuuid());
			ranks.add(rank);
		}
	}

	@Transactional(readOnly = false)
	public String updateUserProfile(User user) {
		// 사용자 프로필 업데이트
		updateUserDetails(user);

		// 랭크 업데이트
		updateUserRanks(user);
		return user.getPuuid();
	}

	private void updateUserDetails(User user) {
		SummonerDto summoner = riotService.getSummonerByPuuid(user.getPuuid());
		user.updateBySummonerDto(summoner);
		userRepository.save(user); // 사용자를 저장하여 변경 사항을 반영
	}

	private void updateUserRanks(User user) {
		Set<RankDto> rankDtos = riotService.getRankBySummonerId(user.getSummonerId());

		// 랭크 DTO로부터 랭크 객체를 업데이트
		updateRank(user, rankDtos, "RANKED_SOLO_5x5");
		updateRank(user, rankDtos, "RANKED_FLEX_SR");
	}

	private void updateRank(User user, Set<RankDto> rankDtos, String queueType) {
		RankDto rankDto = rankDtos.stream()
			.filter(rank -> rank.getQueueType().equals(queueType))
			.findFirst()
			.orElse(null);

		if (rankDto != null) {
			user.getRank().stream()
				.filter(rank -> rank.getQueueType().equals(queueType))
				.findFirst()
				.ifPresent(rank -> {
					rank.updateByRankDto(rankDto);
					rankRepository.save(rank); // 랭크를 업데이트하고 저장
				});
		}
	}

	@Transactional
	public UserProfileDto getUserProfileByNameTag(String name, String tag) {
		Optional<User> userOptional = userRepository.findBySummonerNameAndTagLineAllIgnoreCase(name, tag);
		if (userOptional.isEmpty()) {
			try {
				String puuid = setUserProfile(name, tag);
				if (puuid == null) {
					throw new ResponseStatusException(HttpStatus.NOT_FOUND, "summoner not found");
				}
				userOptional = userRepository.findById(puuid);
			} catch (Exception e) {
				if (e instanceof ResponseStatusException) {
					throw e;
				}
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "summoner not found");
			}
		} else { // 2시간이 지나면 업데이트
			User user = userOptional.get();
			LocalDateTime lastUpdatedAt = user.getUpdatedAt();
			LocalDateTime now = LocalDateTime.now();
			if (Duration.between(lastUpdatedAt, now).toHours() >= 2) {
				updateUserProfile(user);
			}
		}
		UserProfileDto userProfileDto = new UserProfileDto();
		if (userOptional.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "summoner not found");
		}
		User user = userOptional.get();
		userProfileDto.setByUser(user, resourceURL);
		return userProfileDto;
	}
}

