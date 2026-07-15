package com.claro.ordermanager.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI vmsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("VMS Manager API")
                        .description("API para cadastro e gerenciamento de máquinas virtuais")
                        .version("1.0.0"));
    }
}
