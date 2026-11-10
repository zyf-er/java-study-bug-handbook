package com.example.mentalhealth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SendMessageRequest(
        @NotBlank(message = "消息内容不能为空")
        @Size(max = 4000, message = "消息内容不能超过4000个字符")
        String content
) {
}
