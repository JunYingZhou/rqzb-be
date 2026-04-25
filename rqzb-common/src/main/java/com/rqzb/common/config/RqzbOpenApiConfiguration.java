package com.rqzb.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class RqzbOpenApiConfiguration {

    private static final String SA_TOKEN_SCHEME = "satoken";

    @Bean
    public OpenAPI rqzbOpenApi(@Value("${spring.application.name:rqzb-service}") String applicationName) {
        return new OpenAPI()
                .servers(Collections.emptyList())
                .info(new Info()
                        .title(resolveTitle(applicationName))
                        .version("v1")
                        .description("RQZB service API documentation"))
                .components(new Components().addSecuritySchemes(
                        SA_TOKEN_SCHEME,
                        new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name(SA_TOKEN_SCHEME)
                                .description("Fill in the token returned by /login")))
                .addSecurityItem(new SecurityRequirement().addList(SA_TOKEN_SCHEME));
    }

    private String resolveTitle(String applicationName) {
        return switch (applicationName) {
            case "rqzb-auth-service" -> "RQZB Auth API";
            case "rqzb-system-service" -> "RQZB System API";
            case "rqzb-renqing-service" -> "RQZB Renqing API";
            case "rqzb-userinfo-service" -> "RQZB UserInfo API";
            default -> "RQZB API";
        };
    }
}
