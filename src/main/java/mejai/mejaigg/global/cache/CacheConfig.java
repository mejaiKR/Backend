package mejai.mejaigg.global.cache;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
@EnableCaching
public class CacheConfig {

	@Bean
	public CacheManager cacheManager() {
		SimpleCacheManager cacheManager = new SimpleCacheManager();

		CaffeineCache oauthApiCache = new CaffeineCache(CacheNames.OAUTH_API_CACHE, Caffeine.newBuilder()
			.expireAfterWrite(30, TimeUnit.MINUTES)
			.maximumSize(100)
			.recordStats()
			.build()
		);

		CaffeineCache mejaiApiCache = new CaffeineCache(CacheNames.MEJAI_API_CACHE, Caffeine.newBuilder()
			.expireAfterWrite(1, TimeUnit.MINUTES)
			.maximumSize(100)
			.recordStats()
			.build()
		);

		cacheManager.setCaches(List.of(oauthApiCache, mejaiApiCache));
		return cacheManager;
	}
}
