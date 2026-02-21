package com.mkvillage.mkvillage_dairy_service.client;

import com.mkvillage.mkvillage_dairy_service.config.OpenApiConfig;
import com.mkvillage.mkvillage_dairy_service.dto.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "user-management-service",
        url = "http://localhost:8080",
        configuration = OpenApiConfig.class
)
public interface UserClient {

    // 🔹 Internal communication endpoint
    @GetMapping("/internal/users/by-mobile/{mobile}")
    UserResponseDto getUserByMobile(
            @PathVariable("mobile") String mobile
    );

    @GetMapping("/internal/users/{id}")
    UserResponseDto getUserById(
            @PathVariable("id") Long id
    );
}
