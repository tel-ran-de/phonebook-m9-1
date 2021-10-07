package com.telran.phonebookapi.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class SpringFoxConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .ignoredParameterTypes(Authentication.class)
                .securityContexts(List.of(securityContext()))
                .securitySchemes(List.of(apiKey()))
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Phone book REST API",
                "This is project of students Tel-Ran.de college" +
                        "\nUse test user token" +
                        "\neyJhbGciOiJIUzUxMiJ9.eyJ1c2VybmFtZSI6InRlc3RAZ21haWwuY29tIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImV4cCI6NDc4OTIxMzEwNn0.XwUVvUu5Ep98BvLQAZ9baJ6mGCvesRfjfT6puHvX7Ukk1IR9Aq4_Mgbazkkv1rB5BScenuy0kq8zk8sqr8EUrQ",
                "1.0",
                "",
                new Contact("Tel-Ran.de", "https://www.tel-ran.de", "go@tel-ran.de"),
                "",
                "",
                Collections.emptyList());
    }

    //eyJhbGciOiJIUzUxMiJ9.eyJ1c2VybmFtZSI6InRlc3RAZ21haWwuY29tIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIl0sImV4cCI6NDc4OTIxMzEwNn0.XwUVvUu5Ep98BvLQAZ9baJ6mGCvesRfjfT6puHvX7Ukk1IR9Aq4_Mgbazkkv1rB5BScenuy0kq8zk8sqr8EUrQ
    private ApiKey apiKey() {
        return new ApiKey("JWT", "Access-Token", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth())
//                .operationSelector(PathSelectors.regex())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return List.of(new SecurityReference("JWT", authorizationScopes));
    }
}
