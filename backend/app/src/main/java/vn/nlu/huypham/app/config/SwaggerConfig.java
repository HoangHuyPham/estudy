package vn.nlu.huypham.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        String bearerAuth = "Bearer Token";
        String cookieAuth = "Cookie Auth";

        return new OpenAPI()
                .info(new Info()
                        .title("Estudy API")
                        .version("v1")
                        .description("API hỗ trợ cả Bearer Header và HttpOnly Cookie"))
                // Thêm cả 2 yêu cầu bảo mật vào danh sách chung
                .addSecurityItem(new SecurityRequirement()
                        .addList(bearerAuth)
                        .addList(cookieAuth))
                .components(new Components()
                        // Cấu hình 1: Cho phép dán Token vào Header Authorization
                        .addSecuritySchemes(bearerAuth, new SecurityScheme()
                                .name(bearerAuth)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT"))
                        // Cấu hình 2: Cho phép trình duyệt tự gửi Cookie accessToken
                        .addSecuritySchemes(cookieAuth, new SecurityScheme()
                                .name("accessToken") // Tên cookie thực tế
                                .type(SecurityScheme.Type.APIKEY)
                                                        .in(SecurityScheme.In.COOKIE)));

    }
}