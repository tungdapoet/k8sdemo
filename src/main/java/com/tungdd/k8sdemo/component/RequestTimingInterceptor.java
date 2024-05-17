package com.tungdd.k8sdemo.component;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Component
@AllArgsConstructor
public class RequestTimingInterceptor implements HandlerInterceptor {

    private final MeterRegistry meterRegistry;

    @PostConstruct
    public void configureMetrics() {
        meterRegistry.config().meterFilter(new MeterFilter() {
            @Override
            public DistributionStatisticConfig configure(Meter.Id id, DistributionStatisticConfig config) {
                if (id.getName().equals("http_requests_seconds")) {
                    return DistributionStatisticConfig.builder()
                            .percentilesHistogram(true)
                            .percentiles(0.5, 0.95, 0.99)
                            .serviceLevelObjectives(
                                    TimeUnit.MILLISECONDS.toNanos(100),
                                    TimeUnit.MILLISECONDS.toNanos(200),
                                    TimeUnit.MILLISECONDS.toNanos(300),
                                    TimeUnit.MILLISECONDS.toNanos(400),
                                    TimeUnit.MILLISECONDS.toNanos(500),
                                    TimeUnit.MILLISECONDS.toNanos(1000),
                                    TimeUnit.MILLISECONDS.toNanos(1500),
                                    TimeUnit.MILLISECONDS.toNanos(2000),
                                    TimeUnit.MILLISECONDS.toNanos(3000),
                                    TimeUnit.MILLISECONDS.toNanos(5000),
                                    TimeUnit.MILLISECONDS.toNanos(10000)
                            )
                            .build()
                            .merge(config);
                }
                return config;
            }
        });
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute("startTime", System.nanoTime());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        long startTime = (long) request.getAttribute("startTime");
        long duration = System.nanoTime() - startTime;

        Timer.builder("http_requests_seconds")
                .tags("method", request.getMethod(), "uri", request.getRequestURI(), "status", String.valueOf(response.getStatus()))
                .register(meterRegistry)
                .record(duration, TimeUnit.NANOSECONDS);
    }
}



