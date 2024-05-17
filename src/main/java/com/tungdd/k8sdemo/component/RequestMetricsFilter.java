package com.tungdd.k8sdemo.component;

import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.servlet.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class RequestMetricsFilter implements Filter {

    private final AtomicLong requestCount = new AtomicLong(0);
    private final AtomicLong lastRequestCount = new AtomicLong(0);
    private final AtomicLong lastTimeStamp = new AtomicLong(System.currentTimeMillis());
    private final DistributionSummary requestsPerSecond;

    private final MeterRegistry meterRegistry;

    public RequestMetricsFilter(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.requestsPerSecond = DistributionSummary.builder("http.requests.per.second")
                .description("Requests per second")
                .register(meterRegistry);
    }

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);
        long currentTime = System.currentTimeMillis();
        long currentRequestCount = requestCount.incrementAndGet();

        long timeDifference = currentTime - lastTimeStamp.get();
        long requestDifference = currentRequestCount - lastRequestCount.get();

        if (timeDifference > 0) {
            double rps = (requestDifference * 1000.0) / timeDifference;
            meterRegistry.gauge("requests.per.second", rps);
            requestsPerSecond.record(rps);
            System.out.println("Requests per second: " + rps);
        }

        lastTimeStamp.set(currentTime);
        lastRequestCount.set(currentRequestCount);
    }

    @Override
    public void destroy() {
    }
}
