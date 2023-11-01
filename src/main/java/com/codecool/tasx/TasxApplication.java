package com.codecool.tasx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class TasxApplication {

	public static void main(String[] args) {
		SpringApplication.run(TasxApplication.class, args);
	}

}
