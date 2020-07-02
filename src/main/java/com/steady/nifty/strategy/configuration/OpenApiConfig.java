package com.steady.nifty.strategy.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI(@Value("${steady.version}") String appVersion) {
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearerToken",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
                .info(new Info().title("Steady Application API").version(appVersion)
                        .description("The Steady Spring Boot RESTful service using springdoc-openapi and OpenAPI 3."));
    }
}