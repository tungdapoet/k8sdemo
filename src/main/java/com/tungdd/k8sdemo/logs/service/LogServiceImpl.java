package com.tungdd.k8sdemo.logs.service;

import com.tungdd.k8sdemo.logs.entity.LogEntity;
import com.tungdd.k8sdemo.logs.repository.LogRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class LogServiceImpl implements LogService {

    private LogRepository logRepository;

    @Override
    public void saveOrUpdate(LogEntity logEntity) {
        logRepository.save(logEntity);
    }

    @Override
    public Long getRequestCount(Date timeStamp) {
        return logRepository.countLogEntityByTimestampAfter(timeStamp);
    }
}
