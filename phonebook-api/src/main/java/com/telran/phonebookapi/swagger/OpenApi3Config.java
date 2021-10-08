package com.telran.phonebookapi.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class OpenApi3Config {
    private static final String API_KEY = "JWT";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(API_KEY, apiKeySecuritySchema()))
                .info(apiInfo())
                .security(Collections.singletonList(new SecurityRequirement().addList(API_KEY)));
    }

    private Info apiInfo() {
        return new Info()
                .title("Phone book REST API")
                .description("This is project of students Tel-Ran.de college")
                .version("2.0")
                .contact(apiContact())
                .license(null);
    }

    private Contact apiContact() {
        return new Contact()
                .name("Tel-Ran.de")
                .email("go@tel-ran.de")
                .url("https://www.tel-ran.de");
    }

    public SecurityScheme apiKeySecuritySchema() {
        return new SecurityScheme()
                .name("Access-Token")
                .description("The activation token(JWT) can be taken along the path: User -> Get JWT of test user")
                .in(SecurityScheme.In.HEADER)
                .type(SecurityScheme.Type.APIKEY);
    }
}
