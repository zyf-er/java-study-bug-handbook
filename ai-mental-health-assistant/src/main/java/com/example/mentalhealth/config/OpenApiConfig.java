package com.example.mentalhealth.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI mentalHealthOpenApi() {
        return new OpenAPI().info(new Info()
                .title("AI心理健康陪伴助手 API")
                .version("1.0.0")
                .description("学习用途的情绪陪伴服务，不提供医疗诊断或治疗建议。"));
    }
}
