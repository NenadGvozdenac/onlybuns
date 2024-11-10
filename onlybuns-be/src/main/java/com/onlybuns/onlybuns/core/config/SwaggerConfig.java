package com.onlybuns.onlybuns.core.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
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

        // Define the requestBody for multipart/form-data (file upload)
        RequestBody multipartFormDataBody = new RequestBody()
            .content(new io.swagger.v3.oas.models.media.Content()
                .addMediaType("multipart/form-data", new MediaType()
                    .schema(new Schema().type("object")
                        .addProperty("file", new Schema().type("string").format("binary")))));  // Define file as binary type

        // Return OpenAPI configuration with security and multipart setup
        return new OpenAPI()
                .info(new Info()
                    .title("OnlyBuns API")
                    .version("1.0")
                    .description("API documentation for OnlyBuns application."))
                .addSecurityItem(securityRequirement)  // Apply security globally
                .components(new Components()
                    .addSecuritySchemes("bearerAuth", bearerAuth))  // Register the security scheme
                .paths(new io.swagger.v3.oas.models.Paths()
                    .addPathItem("/api/images/upload", new io.swagger.v3.oas.models.PathItem()
                        .post(new io.swagger.v3.oas.models.Operation()
                            .requestBody(multipartFormDataBody)
                            .description("Upload an image file")))); // Attach multipart/form-data specification
    }
}