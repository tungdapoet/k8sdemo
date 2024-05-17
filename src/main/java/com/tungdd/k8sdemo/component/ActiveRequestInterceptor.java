package com.tungdd.k8sdemo.component;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Gauge;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ActiveRequestInterceptor implements HandlerInterceptor {

    private final MeterRegistry meterRegistry;
    private AtomicInteger activeRequests;

    public ActiveRequestInterceptor(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @PostConstruct
    public void initMetrics() {
        activeRequests = new AtomicInteger(0);
        Gauge.builder("http_requests_active", activeRequests, AtomicInteger::get)
                .description("Number of active HTTP requests")
                .register(meterRegistry);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        activeRequests.incrementAndGet();
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        activeRequests.decrementAndGet();
    }
}

