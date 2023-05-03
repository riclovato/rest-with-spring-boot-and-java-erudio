package com.ricklovato.erudio.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//Classe de configuração do Swagger

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Restful API with Java and Spring Boot")
                        .version("v1")
                        .description("some description about API")
                        .termsOfService("https://someurl")
                        .license(new License().name("Apache 2.0").url("https://someurl")));
    }
}
