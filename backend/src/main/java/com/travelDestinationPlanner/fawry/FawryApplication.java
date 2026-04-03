package com.travelDestinationPlanner.fawry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.travelDestinationPlanner.fawry.client")
public class FawryApplication {

	public static void main(String[] args) {
		SpringApplication.run(FawryApplication.class, args);
	}

}
