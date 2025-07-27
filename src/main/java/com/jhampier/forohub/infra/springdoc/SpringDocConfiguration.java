package com.jhampier.forohub.infra.springdoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SpringDocConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Foro Hub API")
                        .description(
                                "API REST para gestión de un foro de discusión con tópicos, respuestas, usuarios y cursos. "
                                        +
                                        "Este sistema permite crear y gestionar discusiones organizadas por cursos, " +
                                        "con autenticación JWT y diferentes niveles de permisos.")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Jhampier")
                                .email("contacto@forohub.com")
                                .url("https://github.com/jhampier"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080/api")
                                .description("Servidor de desarrollo"),
                        new Server()
                                .url("https://api.forohub.com/api")
                                .description("Servidor de producción")))
                .addSecurityItem(new SecurityRequirement()
                        .addList("bearer-jwt"))
                .components(new Components()
                        .addSecuritySchemes("bearer-jwt", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")
                                .description("Proporciona el token JWT en el formato: Bearer {token}")));
    }
}