package com.tungdd.k8sdemo.logs.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Date timestamp = new Date();

    @Column(nullable = false)
    private Long code;

    @Column(nullable = false)
    private Long responseTime;

    @Column(nullable = false)
    private String endpoint;


}
