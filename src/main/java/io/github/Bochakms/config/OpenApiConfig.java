package io.github.Bochakms.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI userServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("User Service API")
                        .description("API for managing users in the system")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Bochakms")
                                .url("https://github.com/Bochakms/user-service-spring")));
    }
}
