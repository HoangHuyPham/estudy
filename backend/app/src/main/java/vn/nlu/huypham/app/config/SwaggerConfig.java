package vn.nlu.huypham.app.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.Components;

@Configuration
public class SwaggerConfig {

        @Bean
        public OpenAPI openAPI() {
                String bearerAuth = "Bearer Token";
                String cookieAuth = "Cookie Auth";

                return new OpenAPI()
                                .servers(List.of(new Server().url("http://localhost:5555").description("Nginx Proxy")))
                                .info(new Info()
                                                .title("Estudy API")
                                                .version("v1")
                                                .description("API hỗ trợ cả Bearer Header và HttpOnly Cookie"))
                                .addSecurityItem(new SecurityRequirement()
                                                .addList(bearerAuth)
                                                .addList(cookieAuth))
                                .components(new Components()
                                                .addSecuritySchemes(bearerAuth, new SecurityScheme()
                                                                .name(bearerAuth)
                                                                .type(SecurityScheme.Type.HTTP)
                                                                .scheme("bearer")
                                                                .bearerFormat("JWT"))
                                                .addSecuritySchemes(cookieAuth, new SecurityScheme()
                                                                .name("accessToken")
                                                                .type(SecurityScheme.Type.APIKEY)
                                                                .in(SecurityScheme.In.COOKIE)));

        }
}