package com.example.Tatkal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication()
@EnableScheduling
public class TatkalApplication {

	public static void main(String[] args) {
		SpringApplication.run(TatkalApplication.class, args);
	}

}
