package mejai.mejaigg.app.user.service;

import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import mejai.mejaigg.app.jwt.JwtProvider;
import mejai.mejaigg.app.user.domain.AppUser;
import mejai.mejaigg.app.user.domain.Relationship;
import mejai.mejaigg.app.user.domain.SocialType;
import mejai.mejaigg.app.user.dto.LoginResponse;
import mejai.mejaigg.app.user.dto.RefreshResponse;
import mejai.mejaigg.app.user.repository.AppUserRepository;
import mejai.mejaigg.summoner.domain.Summoner;

@Service
@RequiredArgsConstructor
public class UserService {

	private final AppUserRepository appUserRepository;
	private final JwtProvider jwtProvider;

	public LoginResponse loginOrSignUp(String socialId, SocialType socialType, String authCode) {
		AppUser appUser = appUserRepository.findAppUserBySocialIdAndSocialType(socialId, socialType)
			.orElseGet(() -> appUserRepository.save(new AppUser(socialId, socialType)));

		return new LoginResponse(
			jwtProvider.generateAccessToken(appUser.getId()),
			jwtProvider.generateRefreshToken(appUser.getId())
		);
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

	public RefreshResponse refresh(String refreshToken) {
		try {
			Long id = jwtProvider.extractId(refreshToken);

			return new RefreshResponse(jwtProvider.generateAccessToken(id), jwtProvider.generateRefreshToken(id));
		} catch (ExpiredJwtException e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "토큰이 만료되었습니다.");
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다.");
		}
	}

	public void delete(Long userId) {
		appUserRepository.deleteById(userId);
	}
}
