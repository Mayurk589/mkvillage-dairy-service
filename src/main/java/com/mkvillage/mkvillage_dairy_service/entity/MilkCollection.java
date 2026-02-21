package com.mkvillage.mkvillage_dairy_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "milk_collection")
@Getter
@Setter
public class MilkCollection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long farmerId;

    @Column(nullable = false)
    private Long dairyId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MilkType milkType;

    @Column(nullable = false)
    private Double quantity;

    @Column(nullable = false)
    private Double fat;

    @Column(nullable = false)
    private Double pricePerLiter;

    @Column(nullable = false)
    private Double totalAmount;

    private LocalDate collectionDate = LocalDate.now();

    private LocalDateTime createdAt = LocalDateTime.now();
}
