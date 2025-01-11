package config;

import org.springframework.context.annotation.Configuration;
import com.google.common.util.concurrent.RateLimiter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class RateLimitConfig {
    private static final Map<String, RateLimiter> rateLimiters = new ConcurrentHashMap<>();
    private static final int REQUESTS_PER_SECOND = 1;
    
    public static void checkRateLimit(String clientId) {
        RateLimiter limiter = rateLimiters.computeIfAbsent(clientId,
            k -> RateLimiter.create(REQUESTS_PER_SECOND));
            
        if (!limiter.tryAcquire(1, TimeUnit.SECONDS)) {
            throw new SecurityException("Rate limit exceeded");
        }
    }
    
    // Cleanup old rate limiters
    @Scheduled(fixedRate = 3600000) // Every hour
    public static void cleanupRateLimiters() {
        long now = System.currentTimeMillis();
        rateLimiters.entrySet().removeIf(entry -> 
            now - entry.getValue().getLastUpdateTime() > TimeUnit.HOURS.toMillis(1));
    }
} 