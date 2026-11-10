package com.example.taskmanagement.dto;

import com.example.taskmanagement.common.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskStatusRequest {

    @NotNull(message = "任务状态不能为空")
    private TaskStatus status;
}

