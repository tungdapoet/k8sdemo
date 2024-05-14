package com.tungdd.k8sdemo.metrics.controller;

import com.tungdd.k8sdemo.logs.service.LogService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.Calendar;

@RestController
@AllArgsConstructor
public class MetricsController {

    private final LogService logService;

    @Value("${metrics.averagingperiod}")
    private final double averagingPeriod = 60.0;

    @GetMapping(value = "/metrics", produces = "text/plain")
    public String metrics() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, -(int) averagingPeriod);
        java.util.Date startTime = calendar.getTime();

        double rate = logService.getRequestCount(startTime) / averagingPeriod;

        String metrics = """
            # HELP request_per_second The number of requests per second.
            # TYPE request_per_second gauge
            request_per_second %f
            # HELP averaging_period The averaging period for request rate.
            # TYPE averaging_period gauge
            averaging_period %f
            """.formatted(rate, averagingPeriod);

        return metrics.trim();
    }
}

