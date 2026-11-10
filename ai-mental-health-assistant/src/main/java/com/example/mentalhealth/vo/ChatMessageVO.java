package com.example.mentalhealth.vo;

import java.time.LocalDateTime;

public record ChatMessageVO(Long id, Long sessionId, String role, String content, LocalDateTime createdAt) {
}
