package com.stablespringbootproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class StablespringbootprojectApplication {

	public static void main(String[] args) {
		SpringApplication.run(StablespringbootprojectApplication.class, args);
	}

}
