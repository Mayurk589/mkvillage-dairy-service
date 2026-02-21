package com.mkvillage.mkvillage_dairy_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "dairy")
@Getter
@Setter
public class Dairy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // This is userId of DAIRY_OWNER from user-service
    @Column(nullable = false)
    private Long ownerId;

    @Column(nullable = false, unique = true)
    private String dairyName;

    private String location;

    @Column(nullable = false)
    private String phoneNumber;

    private boolean active = true;

    private LocalDateTime createdAt = LocalDateTime.now();
}
