package com.smartlogistics.slo.domain.task;

import java.time.Instant;
import java.util.UUID;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "delivery_tasks")
public class TaskEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "external_ref")
    private String externalRef;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "lat", nullable = false)
    private double lat;

    @Column(name = "lng", nullable = false)
    private double lng;

    @Column(name = "window_start")
    private Instant windowStart;

    @Column(name = "window_end")
    private Instant windowEnd;

    @Column(name = "service_minutes", nullable = false)
    private int serviceMinutes;

    @Column(name = "priority", nullable = false)
    private int priority = 0;

    @Column(name = "status", nullable = false)
    private String status = "PLANNED";

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    void onCreate(){
        var now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void onUpdate(){
        updatedAt = Instant.now();
    } 
    
}
