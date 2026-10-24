package com.sgs.server;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;

@Component
public class IpRateLimitFilter extends OncePerRequestFilter {

    private final Cache<String, Bucket> buckets = Caffeine.newBuilder()
            .maximumSize(100_000)
            .expireAfterAccess(Duration.ofHours(1))
            .build();

    private Bucket newBucket() {
        Bandwidth limit = Bandwidth.classic(
                100,
                Refill.greedy(100, Duration.ofMinutes(1))
        );

        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String ip = request.getRemoteAddr();

        Bucket bucket = buckets.get(ip, ignored -> newBucket());
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        if (!probe.isConsumed()) {
            response.setStatus(429);
            response.setContentType("application/json");
            response.setHeader(
                    "Retry-After",
                    Long.toString(
                            Math.max(
                                    1,
                                    (probe.getNanosToWaitForRefill()
                                            + 999_999_999L) / 1_000_000_000L
                            )
                    )
            );
            response.getWriter().write(
                    "{\"error\":\"Too many requests. Try again later.\"}"
            );
            return;
        }

        response.setHeader(
                "X-Rate-Limit-Remaining",
                Long.toString(probe.getRemainingTokens())
        );

        filterChain.doFilter(request, response);
    }
}
