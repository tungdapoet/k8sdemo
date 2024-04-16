package com.tungdd.k8sdemo.info.controller;

import com.tungdd.k8sdemo.info.entity.InfoEntity;
import com.tungdd.k8sdemo.logs.entity.LogEntity;
import com.tungdd.k8sdemo.logs.service.LogService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.net.InetAddress;
import java.util.Random;

@RestController
@Getter
@Setter
public class InfoController {


    private LogService logService;

    private InetAddress address;
    private final Random random = new Random();

    @GetMapping("/info")
    public InfoEntity info() throws InterruptedException {
        long latency = (long) (random.nextFloat() * 10);
        Thread.sleep(latency);
        LogEntity log = LogEntity.builder()
                .code(200L)
                .endpoint("/info")
                .responseTime(latency).build();
        logService.saveOrUpdate(log);
        return new InfoEntity(address.getHostName(), latency);
    }
}

