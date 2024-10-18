package com.onlybuns.onlybuns.core.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // Define the security scheme (Bearer token)
        SecurityScheme bearerAuth = new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
            .description("JWT-based authentication. Enter your JWT token prefixed with 'Bearer '.");

        // Add the security requirement to the API documentation
        SecurityRequirement securityRequirement = new SecurityRequirement()
            .addList("bearerAuth");

        return new OpenAPI()
                .info(new Info()
                    .title("OnlyBuns API")
                    .version("1.0")
                    .description("API documentation for OnlyBuns application."))
                .addSecurityItem(securityRequirement) // Apply security globally
                .components(new Components()
                    .addSecuritySchemes("bearerAuth", bearerAuth)); // Register the security scheme
    }
}