package mejai.mejaigg.app.jwt.client;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import mejai.mejaigg.app.jwt.dto.JwksResponse;

@FeignClient(
	name = "apple",
	url = "https://appleid.apple.com"
)
public interface AppleClient extends JwksClient {
	@Cacheable(cacheNames = "apiCache")
	@GetMapping("/auth/keys")
	JwksResponse getJwks();
}
