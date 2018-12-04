package com.arma.inz.compcal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
@EnableScheduling
public class CompcalApplication {

	public static void main(String[] args) {
		SpringApplication.run(CompcalApplication.class, args);
	}
}