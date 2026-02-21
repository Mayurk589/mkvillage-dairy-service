package com.mkvillage.mkvillage_dairy_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MkvillageDairyServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MkvillageDairyServiceApplication.class, args);
	}

}
