package com.mkvillage.mkvillage_dairy_service.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserResponseDto {

    private Long id;
    private String name;
    private String mobile;
    private String email;
    private boolean active;
    private Set<String> roles;
}
