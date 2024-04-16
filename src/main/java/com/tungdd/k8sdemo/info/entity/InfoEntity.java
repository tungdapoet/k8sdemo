package com.tungdd.k8sdemo.info.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class InfoEntity {
    private String Hostname;
    private Long Latency;
}
