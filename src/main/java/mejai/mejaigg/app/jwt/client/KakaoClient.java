package mejai.mejaigg.app.jwt.client;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import mejai.mejaigg.app.jwt.dto.JwksResponse;

@FeignClient(
	name = "kakao",
	url = "https://kauth.kakao.com"
)
public interface KakaoClient {
	@Cacheable(cacheNames = "apiCache")
	@GetMapping("/.well-known/jwks.json")
	JwksResponse getJwks();
}
