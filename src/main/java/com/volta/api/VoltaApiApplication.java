package com.volta.api;

import com.volta.api.config.AdminProperties;
import com.volta.api.config.SwaggerProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({AdminProperties.class, SwaggerProperties.class})
public class VoltaApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(VoltaApiApplication.class, args);
    }

}
