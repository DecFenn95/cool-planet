package com.example.coolplanet.service.data;

import com.example.coolplanet.entity.TaskTypeEvent;
import com.example.coolplanet.entity.TaskTypeOverview;
import com.example.coolplanet.repository.TaskTypeEventRepository;
import com.example.coolplanet.repository.TaskTypeOverviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class TaskTypeEventDataServiceTest {

    private TaskTypeEventRepository taskTypeEventRepository;

    private TaskTypeEventDataService taskTypeEventDataService;

    @BeforeEach
    void setUp() {
        this.taskTypeEventRepository = mock(TaskTypeEventRepository.class);
        this.taskTypeEventDataService = new TaskTypeEventDataService(this.taskTypeEventRepository);
    }

    @Test
    void getEventsByTaskIdentifier_whenCalled_returnsListOfTaskTypeEvents() {
        var createdAt = LocalDateTime.now().minusMinutes(25);
        var taskTypeEvent = new TaskTypeEvent();
        taskTypeEvent.setTaskIdentifierType("A");
        taskTypeEvent.setDuration(5.0);
        taskTypeEvent.setCreatedAt(createdAt);

        when(this.taskTypeEventRepository.getEventsByTaskIdentifier(anyString())).thenReturn(List.of(taskTypeEvent));

        var result = this.taskTypeEventDataService.getEventsByTaskIdentifier("A");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("A", result.get(0).getTaskIdentifierType());
        assertEquals(5.0, result.get(0).getDuration());
        assertEquals(createdAt, result.get(0).getCreatedAt());
    }

    @Test
    void getEventsSinceLastUpdateByTaskIdentifier_whenCalled_returnsListOfTaskTypeEvents() {
        var createdAt = LocalDateTime.now().minusMinutes(25);
        var taskTypeEvent = new TaskTypeEvent();
        taskTypeEvent.setTaskIdentifierType("A");
        taskTypeEvent.setDuration(5.0);
        taskTypeEvent.setCreatedAt(createdAt);

        when(this.taskTypeEventRepository.getEventsSinceLastUpdateByTaskIdentifier(anyString(), any(LocalDateTime.class))).thenReturn(List.of(taskTypeEvent));

        var result = this.taskTypeEventDataService.getEventsSinceLastUpdateByTaskIdentifier("A", LocalDateTime.now().minusMinutes(50));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("A", result.get(0).getTaskIdentifierType());
        assertEquals(5.0, result.get(0).getDuration());
        assertEquals(createdAt, result.get(0).getCreatedAt());
    }

    @Test
    void createTaskEvent_whenCalled_setsCreatedAtAndCallsCreate() {
        var createdAtCaptor = ArgumentCaptor.forClass(TaskTypeEvent.class);

        var taskTypeEvent = new TaskTypeEvent();
        taskTypeEvent.setTaskIdentifierType("A");
        taskTypeEvent.setDuration(5.0);

        this.taskTypeEventDataService.createTaskEvent(taskTypeEvent);

        verify(this.taskTypeEventRepository, times(1)).save(createdAtCaptor.capture());

        var updatedTaskTypeEvent = createdAtCaptor.getValue();
        assertEquals(taskTypeEvent.getTaskIdentifierType(), updatedTaskTypeEvent.getTaskIdentifierType());
        assertEquals(taskTypeEvent.getDuration(), updatedTaskTypeEvent.getDuration());
        assertNotNull(updatedTaskTypeEvent.getCreatedAt());
    }
}