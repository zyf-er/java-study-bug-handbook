package com.example.mentalhealth.vo;

import java.time.LocalDateTime;

public record ChatSessionVO(Long id, String title, LocalDateTime createdAt, LocalDateTime updatedAt) {
}
