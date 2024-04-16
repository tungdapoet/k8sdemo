package com.tungdd.k8sdemo.metrics.controller;

import com.tungdd.k8sdemo.logs.service.LogService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/metrics")
public class MetricsController {

    private final LogService logService;

    @Value("${metrics.averagingperiod}")
    private final double averagingPeriod = 60.0;

    @GetMapping(value = "/getMetrics", produces = "application/json")
    public Map<String, Object> metrics() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, -(int) averagingPeriod);
        java.util.Date startTime = calendar.getTime();

        double rate = logService.getRequestCount(startTime) / averagingPeriod;

        Map<String, Object> metrics = new HashMap<>();
        metrics.put("request_per_second", rate);
        metrics.put("averaging_period", averagingPeriod);

        return metrics;
    }
}

