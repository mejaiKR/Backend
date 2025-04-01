package mejai.mejaigg.app.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import mejai.mejaigg.app.user.domain.AppUser;
import mejai.mejaigg.app.user.domain.SocialType;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
	Optional<AppUser> findAppUserBySocialIdAndSocialType(String socialId, SocialType socialType);
}
