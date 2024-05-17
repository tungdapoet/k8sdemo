package com.tungdd.k8sdemo.metrics.controller;

import io.micrometer.prometheus.PrometheusMeterRegistry;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class MetricsController {

    private final PrometheusMeterRegistry prometheusMeterRegistry;

    @GetMapping(value = "/metrics", produces = "text/plain")
    public String metrics() {
        return prometheusMeterRegistry.scrape();
    }
}

