package com.mkvillage.mkvillage_dairy_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "farmer_dairy_mapping",
       uniqueConstraints = @UniqueConstraint(
               columnNames = {"farmerId", "dairyId"}
       ))
@Getter
@Setter
public class FarmerDairyMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long farmerId;

    @Column(nullable = false)
    private Long dairyId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MappingStatus status;

    private LocalDateTime requestedAt = LocalDateTime.now();

    private LocalDateTime respondedAt;
}
