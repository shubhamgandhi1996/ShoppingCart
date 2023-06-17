package com.shubhamgandhi.serviceregistery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer //this will enable the service discovery server
//so that server will know what all applications are there which are connectyed to the server
public class ServiceRegisteryApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceRegisteryApplication.class, args);
	}

}
