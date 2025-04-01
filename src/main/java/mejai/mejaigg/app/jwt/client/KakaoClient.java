package mejai.mejaigg.app.jwt.client;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import mejai.mejaigg.app.jwt.dto.JwksResponse;
import mejai.mejaigg.global.cache.CacheNames;

@FeignClient(
	name = "kakao",
	url = "https://kauth.kakao.com"
)
public interface KakaoClient extends JwksClient {
	@Cacheable(cacheNames = CacheNames.OAUTH_API_CACHE, key = "'kakao-jwks'")
	@GetMapping("/.well-known/jwks.json")
	JwksResponse getJwks();
}
