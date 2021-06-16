package com.brsrker.emerald.jwt.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EntityScan("com.brsrker.emerald.jwt.auth.entity")
@SpringBootApplication
public class EmeraldJWTAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmeraldJWTAuthApplication.class, args);
	}

}