package com.ecommerce.api_gateway;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(DiscoveryClient discoveryClient) {
		return args -> {
			System.out.println("DiscoveryClient implementation = "
					+ discoveryClient.getClass().getName());

			System.out.println("Registered services = "
					+ discoveryClient.getServices());
		};
	}
}