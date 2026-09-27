package com.volta.api;

import com.volta.api.config.AdminProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AdminProperties.class)
public class VoltaApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(VoltaApiApplication.class, args);
	}

}
