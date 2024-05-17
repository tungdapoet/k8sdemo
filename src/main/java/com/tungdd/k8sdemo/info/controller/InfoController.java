package com.tungdd.k8sdemo.info.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/info")
public class InfoController {

    @GetMapping("/hostname")
    public Map<String, Object> info() {
        Map<String, Object> info = new HashMap<>();

        try {
            // Get hostname
            InetAddress inetAddress = InetAddress.getLocalHost();
            info.put("hostname", inetAddress.getHostName());
        } catch (UnknownHostException e) {
            info.put("hostname", "Unknown Host");
        }

        // Measure latency
        Instant start = Instant.now();
        Instant end = Instant.now();
        Duration latency = Duration.between(start, end);
        info.put("latency", latency.toMillis() + " ms");

        return info;
    }
}

