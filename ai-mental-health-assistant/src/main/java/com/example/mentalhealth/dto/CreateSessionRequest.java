package com.example.mentalhealth.dto;

import jakarta.validation.constraints.Size;

public record CreateSessionRequest(
        @Size(max = 120, message = "会话标题不能超过120个字符")
        String title
) {
}
