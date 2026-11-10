package com.example.mentalhealth.dto;

import jakarta.validation.constraints.Size;

public record AgentCheckInRequest(
        @Size(max = 200, message = "话题不能超过200个字符")
        String topic
) {
}
