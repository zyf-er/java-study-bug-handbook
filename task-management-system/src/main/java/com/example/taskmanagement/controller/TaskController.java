package com.example.taskmanagement.controller;

import com.example.taskmanagement.common.Result;
import com.example.taskmanagement.common.TaskPriority;
import com.example.taskmanagement.common.TaskStatus;
import com.example.taskmanagement.dto.TaskRequest;
import com.example.taskmanagement.dto.TaskStatusRequest;
import com.example.taskmanagement.service.TaskService;
import com.example.taskmanagement.vo.PageResultVO;
import com.example.taskmanagement.vo.TaskVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/tasks")
@Tag(name = "任务管理")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @Operation(summary = "新增任务")
    public Result<TaskVO> create(@Valid @RequestBody TaskRequest request) {
        return Result.success(taskService.create(request));
    }

    @GetMapping
    @Operation(summary = "分页查询任务")
    public Result<PageResultVO<TaskVO>> findPage(
            @RequestParam(defaultValue = "1") @Min(value = 1, message = "页码必须大于0") long page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "每页条数必须大于0")
            @Max(value = 100, message = "每页最多查询100条") long size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority) {
        return Result.success(taskService.findPage(page, size, title, status, priority));
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询任务详情")
    public Result<TaskVO> findById(@PathVariable @Min(value = 1, message = "任务ID必须大于0") Long id) {
        return Result.success(taskService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改任务")
    public Result<TaskVO> update(
            @PathVariable @Min(value = 1, message = "任务ID必须大于0") Long id,
            @Valid @RequestBody TaskRequest request) {
        return Result.success(taskService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除任务")
    public Result<Void> delete(@PathVariable @Min(value = 1, message = "任务ID必须大于0") Long id) {
        taskService.delete(id);
        return Result.success();
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "修改任务状态")
    public Result<TaskVO> updateStatus(
            @PathVariable @Min(value = 1, message = "任务ID必须大于0") Long id,
            @Valid @RequestBody TaskStatusRequest request) {
        return Result.success(taskService.updateStatus(id, request));
    }
}

