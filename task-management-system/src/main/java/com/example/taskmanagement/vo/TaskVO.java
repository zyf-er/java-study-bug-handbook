package com.example.taskmanagement.vo;

import com.example.taskmanagement.common.TaskPriority;
import com.example.taskmanagement.common.TaskStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TaskVO(
        Long id,
        String title,
        String description,
        TaskStatus status,
        TaskPriority priority,
        LocalDate dueDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

