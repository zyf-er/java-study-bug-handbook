package com.example.taskmanagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI taskManagementOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("任务管理系统 API")
                        .version("1.0.0")
                        .description("任务新增、分页查询、修改、删除和状态管理接口"));
    }
}

