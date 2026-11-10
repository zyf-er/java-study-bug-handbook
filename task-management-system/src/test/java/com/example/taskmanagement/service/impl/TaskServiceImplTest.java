package com.example.taskmanagement.service.impl;

import com.example.taskmanagement.common.TaskPriority;
import com.example.taskmanagement.common.TaskStatus;
import com.example.taskmanagement.dto.TaskRequest;
import com.example.taskmanagement.dto.TaskStatusRequest;
import com.example.taskmanagement.entity.Task;
import com.example.taskmanagement.exception.TaskNotFoundException;
import com.example.taskmanagement.mapper.TaskMapper;
import com.example.taskmanagement.vo.TaskVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskMapper taskMapper;

    private TaskServiceImpl taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskServiceImpl(taskMapper);
    }

    @Test
    void createStoresTaskAndReturnsView() {
        when(taskMapper.insert(any(Task.class))).thenAnswer(invocation -> {
            Task task = invocation.getArgument(0);
            task.setId(12L);
            return 1;
        });
        TaskRequest request = new TaskRequest();
        request.setTitle("  Write tests  ");
        request.setStatus(TaskStatus.TODO);
        request.setPriority(TaskPriority.HIGH);

        TaskVO result = taskService.create(request);

        assertEquals(12L, result.id());
        assertEquals("Write tests", result.title());
        assertNotNull(result.createdAt());
        assertNotNull(result.updatedAt());
        verify(taskMapper).insert(any(Task.class));
    }

    @Test
    void updateStatusChangesTaskStatus() {
        Task task = new Task();
        task.setId(3L);
        task.setTitle("Test task");
        task.setStatus(TaskStatus.TODO);
        task.setPriority(TaskPriority.MEDIUM);
        when(taskMapper.selectById(3L)).thenReturn(task);
        TaskStatusRequest request = new TaskStatusRequest();
        request.setStatus(TaskStatus.DONE);

        TaskVO result = taskService.updateStatus(3L, request);

        assertEquals(TaskStatus.DONE, result.status());
        verify(taskMapper).updateById(task);
    }

    @Test
    void findByIdThrowsWhenTaskDoesNotExist() {
        when(taskMapper.selectById(404L)).thenReturn(null);

        assertThrows(TaskNotFoundException.class, () -> taskService.findById(404L));
    }
}
