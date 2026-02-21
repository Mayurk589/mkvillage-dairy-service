package com.mkvillage.mkvillage_dairy_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "dairy_milk_rate",
       uniqueConstraints = @UniqueConstraint(
               columnNames = {"dairyId"}
       ))
@Getter
@Setter
public class DairyMilkRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long dairyId;

    // Rate per 1% fat
    @Column(nullable = false)
    private Double cowFatRate;

    @Column(nullable = false)
    private Double buffaloFatRate;

    private LocalDateTime updatedAt = LocalDateTime.now();
}
