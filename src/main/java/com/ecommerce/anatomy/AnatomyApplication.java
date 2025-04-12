package com.ecommerce.anatomy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EntityScan(basePackages = "com.ecommerce.anatomy.model")
@EnableScheduling
@EnableTransactionManagement
public class AnatomyApplication {

	public static void main(String[] args) {

		SpringApplication.run(AnatomyApplication.class, args);
		System.out.println("Hellow world!");
	}

}
