package com.tungdd.k8sdemo.health.controller;

import com.tungdd.k8sdemo.health.entity.HealthEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping("/status")
    public HealthEntity getStatus() {
        return new HealthEntity("Running");
    }
}
