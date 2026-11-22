package com.sgs.server.Configurations;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "SGS Server API",
                version = "v1",
                description = "REST API for SGS"
        )
)
public class OpenApiConfig {
}