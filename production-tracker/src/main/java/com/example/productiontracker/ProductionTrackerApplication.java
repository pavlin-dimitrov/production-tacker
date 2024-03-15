package com.example.productiontracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;

@SpringBootApplication
public class ProductionTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductionTrackerApplication.class, args);
	}

}
