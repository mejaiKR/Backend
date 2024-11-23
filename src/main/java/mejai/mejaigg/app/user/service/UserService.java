package mejai.mejaigg.app.user.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mejai.mejaigg.app.jwt.JwtProvider;
import mejai.mejaigg.app.user.domain.AppUser;
import mejai.mejaigg.app.user.domain.Relationship;
import mejai.mejaigg.app.user.domain.SocialType;
import mejai.mejaigg.app.user.dto.LoginResponse;
import mejai.mejaigg.app.user.repository.AppUserRepository;
import mejai.mejaigg.summoner.domain.Summoner;

@Service
@RequiredArgsConstructor
public class UserService {

	private final AppUserRepository appUserRepository;
	private final JwtProvider jwtProvider;

	public LoginResponse loginOrSignUp(String socialId, SocialType socialType) {
		AppUser appUser = appUserRepository.findAppUserBySocialIdAndSocialType(socialId, socialType)
			.orElseGet(() -> appUserRepository.save(new AppUser(socialId, socialType)));

		return new LoginResponse(jwtProvider.generateToken(appUser.getId()));
	}

	public void addWatchSummoner(long userId, Summoner summoner, Relationship relationship) {
		AppUser appUser = appUserRepository.findById(userId).orElseThrow();

		appUser.addWatchSummoner(summoner, relationship);
		appUserRepository.save(appUser);
	}

	public AppUser findUserById(long userId) {
		return appUserRepository.findById(userId).orElseThrow();
	}

	public List<Summoner> findAllWatchSummoner() {
		return appUserRepository.findAll().stream()
			.map(AppUser::getWatchSummoner)
			.filter(Objects::nonNull)
			.distinct()
			.toList();
	}
}
