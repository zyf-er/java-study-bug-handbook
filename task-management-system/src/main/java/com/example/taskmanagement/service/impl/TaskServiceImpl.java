package com.example.taskmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.taskmanagement.common.TaskPriority;
import com.example.taskmanagement.common.TaskStatus;
import com.example.taskmanagement.dto.TaskRequest;
import com.example.taskmanagement.dto.TaskStatusRequest;
import com.example.taskmanagement.entity.Task;
import com.example.taskmanagement.exception.TaskNotFoundException;
import com.example.taskmanagement.mapper.TaskMapper;
import com.example.taskmanagement.service.TaskService;
import com.example.taskmanagement.vo.PageResultVO;
import com.example.taskmanagement.vo.TaskVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskMapper taskMapper;

    public TaskServiceImpl(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    @Override
    @Transactional
    public TaskVO create(TaskRequest request) {
        LocalDateTime now = LocalDateTime.now();
        Task task = new Task();
        copyRequest(request, task);
        task.setCreatedAt(now);
        task.setUpdatedAt(now);
        taskMapper.insert(task);
        return toVO(task);
    }

    @Override
    public PageResultVO<TaskVO> findPage(long page, long size, String title,
                                         TaskStatus status, TaskPriority priority) {
        LambdaQueryWrapper<Task> query = new LambdaQueryWrapper<Task>()
                .like(title != null && !title.isBlank(), Task::getTitle, title == null ? null : title.trim())
                .eq(status != null, Task::getStatus, status)
                .eq(priority != null, Task::getPriority, priority)
                .orderByDesc(Task::getCreatedAt);

        Page<Task> result = taskMapper.selectPage(new Page<>(page, size), query);
        List<TaskVO> records = result.getRecords().stream().map(this::toVO).toList();
        return new PageResultVO<>(result.getCurrent(), result.getSize(), result.getTotal(), records);
    }

    @Override
    public TaskVO findById(Long id) {
        return toVO(getTask(id));
    }

    @Override
    @Transactional
    public TaskVO update(Long id, TaskRequest request) {
        Task task = getTask(id);
        copyRequest(request, task);
        task.setUpdatedAt(LocalDateTime.now());
        taskMapper.updateById(task);
        return toVO(task);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (taskMapper.deleteById(id) == 0) {
            throw new TaskNotFoundException(id);
        }
    }

    @Override
    @Transactional
    public TaskVO updateStatus(Long id, TaskStatusRequest request) {
        Task task = getTask(id);
        task.setStatus(request.getStatus());
        task.setUpdatedAt(LocalDateTime.now());
        taskMapper.updateById(task);
        return toVO(task);
    }

    private Task getTask(Long id) {
        Task task = taskMapper.selectById(id);
        if (task == null) {
            throw new TaskNotFoundException(id);
        }
        return task;
    }

    private void copyRequest(TaskRequest request, Task task) {
        task.setTitle(request.getTitle().trim());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());
    }

    private TaskVO toVO(Task task) {
        return new TaskVO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getDueDate(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}

