package com.example.timetracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Set;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private final Contact contact = new Contact(
            "Support",
            "http://localhost",
            "support@mail.com");

    private final ApiInfo apiInfo = new ApiInfo(
            "Time Tacking REST API",
            "This page documents REST web service endpoints",
            "http://localhost",
            "1.0",
            contact,
            "Apache 2.0",
            "http://localhost",
            new ArrayList<>());

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .protocols(Set.of("HTTP", "HTTPS"))
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }
}
