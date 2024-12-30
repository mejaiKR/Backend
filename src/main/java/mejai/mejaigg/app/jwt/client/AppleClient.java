package mejai.mejaigg.app.jwt.client;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import mejai.mejaigg.app.jwt.dto.JwksResponse;
import mejai.mejaigg.global.cache.CacheNames;

@FeignClient(
	name = "apple",
	url = "https://appleid.apple.com"
)
public interface AppleClient extends JwksClient {
	@Cacheable(cacheNames = CacheNames.OAUTH_API_CACHE)
	@GetMapping("/auth/keys")
	JwksResponse getJwks();
}
