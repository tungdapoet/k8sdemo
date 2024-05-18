package com.tungdd.k8sdemo.component;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import jakarta.servlet.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;

@Component
public class RequestMetricsFilter implements Filter {

    private static final Logger LOGGER = Logger.getLogger(RequestMetricsFilter.class.getName());

    private final AtomicLong requestCount = new AtomicLong(0);  // Total number of requests
    private final AtomicLong lastRequestCount = new AtomicLong(0);  // Last number of requests
    private final AtomicLong lastTimeStamp = new AtomicLong(System.nanoTime());  // Last timestamp
    private final AtomicLong requestsPerSecond = new AtomicLong(0);  // Current RPS

    private final Queue<Long> rpsQueue = new LinkedList<>();  // Queue for smoothing RPS values
    private final int SMOOTHING_WINDOW_SIZE = 10;  // Window size for smoothing

    private static final long MIN_INTERVAL_NS = 50_000_000L;  // Minimum interval of 100 milliseconds

    public RequestMetricsFilter(PrometheusMeterRegistry prometheusMeterRegistry) {
        Gauge.builder("requests.per.second", requestsPerSecond, AtomicLong::get)
                .description("Requests per second")
                .register(prometheusMeterRegistry);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOGGER.info("Initializing RequestMetricsFilter.");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // Introduce random latency between 100ms and 1000ms
        int latency = ThreadLocalRandom.current().nextInt(100, 1001);
        try {
            Thread.sleep(latency);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ServletException("Thread interrupted", e);
        }

        chain.doFilter(request, response);

        // Record the current time at the end of the request
        long endTime = System.nanoTime();

        // Increment the total request count
        long currentRequestCount = requestCount.incrementAndGet();

        // Calculate the time difference and request difference
        long timeDifference = endTime - lastTimeStamp.get();
        long requestDifference = currentRequestCount - lastRequestCount.get();

        // Ensure we have a positive time difference and it's greater than the minimum interval
        if (timeDifference > MIN_INTERVAL_NS) {
            // Calculate RPS
            long rps = (requestDifference * 1_000_000_000L) / timeDifference;
            updateRps(rps);

            // Update the last timestamp and request count
            lastTimeStamp.set(endTime);
            lastRequestCount.set(currentRequestCount);
        }
    }

    @Override
    public void destroy() {
        LOGGER.info("Destroying RequestMetricsFilter.");
    }

    private void updateRps(long rps) {
        // Maintain a smoothing window
        if (rpsQueue.size() >= SMOOTHING_WINDOW_SIZE) {
            rpsQueue.poll();
        }
        rpsQueue.offer(rps);

        // Calculate the average RPS over the smoothing window
        long averageRps = (long) rpsQueue.stream().mapToLong(val -> val).average().orElse(0.0);
        requestsPerSecond.set(averageRps);
    }
}
