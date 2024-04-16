package com.tungdd.k8sdemo.logs.service;

import com.tungdd.k8sdemo.logs.entity.LogEntity;

import java.util.Date;

public interface LogService {
    void saveOrUpdate(LogEntity logEntity);

    Long getRequestCount(Date timeStamp);
}
