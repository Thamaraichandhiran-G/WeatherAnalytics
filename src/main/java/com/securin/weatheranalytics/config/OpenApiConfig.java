package com.securin.weatheranalytics.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Weather Analytics API")
                        .version("1.0.0")
                        .description(
                                "Professional Weather Data Processing and Analytics API for Delhi Weather Dataset (1996-2016)")
                        .contact(new Contact()
                                .name("Developer Support")
                                .email("support@securin.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")));
    }
}
