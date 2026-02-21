package com.mkvillage.mkvillage_dairy_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Getter
@Setter
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long farmerId;

    @Column(nullable = false)
    private Long dairyId;

    @Column(nullable = false)
    private Double amount;

    private String paymentMode; // CASH / UPI / BANK

    private String remarks;

    private LocalDate paymentDate = LocalDate.now();

    private LocalDateTime createdAt = LocalDateTime.now();
}
