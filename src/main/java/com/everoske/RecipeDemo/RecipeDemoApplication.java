package com.everoske.RecipeDemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Insertion point for API
 */
@Slf4j
@SpringBootApplication
public class RecipeDemoApplication {

	public static void main(String[] args) {
		log.info("Starting Spring Server");
		SpringApplication.run(RecipeDemoApplication.class, args);
	}

}
