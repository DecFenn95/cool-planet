package com.example.coolplanet.service.data;

import com.example.coolplanet.entity.TaskTypeOverview;
import com.example.coolplanet.repository.TaskTypeOverviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class TaskTypeOverviewDataServiceTest {

    private TaskTypeOverviewRepository taskTypeOverviewRepository;

    private TaskTypeOverviewDataService taskTypeOverviewDataService;

    @BeforeEach
    void setUp() {
        this.taskTypeOverviewRepository = mock(TaskTypeOverviewRepository.class);
        this.taskTypeOverviewDataService = new TaskTypeOverviewDataService(this.taskTypeOverviewRepository);
    }

    @Test
    void getByTaskIdentifier_whenCalled_returnsTaskTypeOverview() {
        var updatedAt = LocalDateTime.now();
        var taskTypeOverview = new TaskTypeOverview();
        taskTypeOverview.setTaskIdentifierType("A");
        taskTypeOverview.setCurrentAverage(7.0);
        taskTypeOverview.setUpdatedAt(updatedAt);

        when(this.taskTypeOverviewRepository.getByTaskIdentifier(anyString())).thenReturn(taskTypeOverview);

        var result = this.taskTypeOverviewDataService.getByTaskIdentifier("A");

        assertEquals("A", result.getTaskIdentifierType());
        assertEquals(7.0, result.getCurrentAverage());
        assertEquals(updatedAt, result.getUpdateAt());
    }

    @Test
    void updateAverageByTaskIdentifierType_whenCalled_callsUpdateForTaskTypeOverview() {
        this.taskTypeOverviewDataService.updateAverageByTaskIdentifierType("A", 5.0);
        verify(this.taskTypeOverviewRepository, times(1)).updateAverageByTaskIdentifierType(anyDouble(), any(LocalDateTime.class), anyString());
    }
}