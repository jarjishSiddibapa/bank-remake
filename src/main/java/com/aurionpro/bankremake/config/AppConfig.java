package com.aurionpro.bankremake.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class AppConfig {
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	@Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info()
            .title("API title")
            .version("1.0")
            .description("API Description")
        );
    }
}
