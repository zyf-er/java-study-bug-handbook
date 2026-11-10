package com.example.taskmanagement.service;

import com.example.taskmanagement.common.TaskPriority;
import com.example.taskmanagement.common.TaskStatus;
import com.example.taskmanagement.dto.TaskRequest;
import com.example.taskmanagement.dto.TaskStatusRequest;
import com.example.taskmanagement.vo.PageResultVO;
import com.example.taskmanagement.vo.TaskVO;

public interface TaskService {

    TaskVO create(TaskRequest request);

    PageResultVO<TaskVO> findPage(long page, long size, String title, TaskStatus status, TaskPriority priority);

    TaskVO findById(Long id);

    TaskVO update(Long id, TaskRequest request);

    void delete(Long id);

    TaskVO updateStatus(Long id, TaskStatusRequest request);
}

