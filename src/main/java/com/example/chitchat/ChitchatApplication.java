package com.example.chitchat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ChitchatApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChitchatApplication.class, args);
	}

}
