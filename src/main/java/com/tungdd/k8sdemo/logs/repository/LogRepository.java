package com.tungdd.k8sdemo.logs.repository;

import com.tungdd.k8sdemo.logs.entity.LogEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface LogRepository extends CrudRepository<LogEntity, Long> {
    Long countLogEntityByTimestampAfter(Date timeStamp);
}
