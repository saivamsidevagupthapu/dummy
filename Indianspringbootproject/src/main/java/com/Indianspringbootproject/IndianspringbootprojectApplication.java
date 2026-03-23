package com.Indianspringbootproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class IndianspringbootprojectApplication {

	public static void main(String[] args) {
		SpringApplication.run(IndianspringbootprojectApplication.class, args);
	}

}
