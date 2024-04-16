package com.tungdd.k8sdemo.info.controller;

import com.tungdd.k8sdemo.info.entity.InfoEntity;
import com.tungdd.k8sdemo.logs.entity.LogEntity;
import com.tungdd.k8sdemo.logs.service.LogService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

@RestController
@AllArgsConstructor
@RequestMapping("/info")
public class InfoController {


    private final LogService logService;
    private final Random random = new Random();

    @GetMapping("/getInfo")
    public InfoEntity info() throws InterruptedException {

        InetAddress address;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

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

