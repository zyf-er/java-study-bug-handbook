package com.example.taskmanagement.dto;

import com.example.taskmanagement.common.TaskPriority;
import com.example.taskmanagement.common.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskRequest {

    @NotBlank(message = "任务标题不能为空")
    @Size(max = 200, message = "任务标题不能超过200个字符")
    private String title;

    @Size(max = 2000, message = "任务描述不能超过2000个字符")
    private String description;

    @NotNull(message = "任务状态不能为空")
    private TaskStatus status = TaskStatus.TODO;

    @NotNull(message = "任务优先级不能为空")
    private TaskPriority priority = TaskPriority.MEDIUM;

    private LocalDate dueDate;
}

