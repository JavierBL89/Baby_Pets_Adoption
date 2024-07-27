package com.example.bb_pets_adoption;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;

@SpringBootApplication(scanBasePackages = "com.example.bb_pets_adoption")
@EnableScheduling
public class BbPetsAdoptionApplication{

	public static void main(String[] args) {
		SpringApplication.run(BbPetsAdoptionApplication.class, args);
	}

}
