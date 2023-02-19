package com.example.coolplanet.service;

import com.example.coolplanet.entity.TaskTypeEvent;
import com.example.coolplanet.entity.TaskTypeOverview;
import com.example.coolplanet.request.AverageRequest;
import com.example.coolplanet.service.data.TaskTypeEventDataService;
import com.example.coolplanet.service.data.TaskTypeOverviewDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AverageServiceImplTest {

    private TaskTypeEventDataService taskTypeEventDataService;

    private TaskTypeOverviewDataService taskTypeOverviewDataService;

    private AverageService averageService;

    @BeforeEach
    void setUp() {
        this.taskTypeOverviewDataService = mock(TaskTypeOverviewDataService.class);
        this.taskTypeEventDataService = mock(TaskTypeEventDataService.class);
        this.averageService = new AverageServiceImpl(this.taskTypeOverviewDataService, this.taskTypeEventDataService);
    }

    @Test
    void calculateNewAverage_whenTaskIdentifierPassedAndNoNewEventsSinceLastUpdate_useCurrentAverage() {
        var taskIdentifierCaptor = ArgumentCaptor.forClass(String.class);
        var lastUpdatedAtCaptor = ArgumentCaptor.forClass(LocalDateTime.class);

        var newAvgCaptor = ArgumentCaptor.forClass(Double.class);
        var updateTaskIdentifierCaptor = ArgumentCaptor.forClass(String.class);

        var taskTypeOverview = new TaskTypeOverview();
        taskTypeOverview.setTaskIdentifierType("A");
        taskTypeOverview.setCurrentAverage(7.0);
        taskTypeOverview.setUpdatedAt(LocalDateTime.now());

        var taskTypeEvent = new TaskTypeEvent();
        taskTypeEvent.setTaskIdentifierType("A");
        taskTypeEvent.setDuration(5.0);
        taskTypeEvent.setCreatedAt(LocalDateTime.now().minusMinutes(25));

        var oldTaskTypeEvent = new TaskTypeEvent();
        oldTaskTypeEvent.setTaskIdentifierType("A");
        oldTaskTypeEvent.setDuration(7.0);
        oldTaskTypeEvent.setCreatedAt(LocalDateTime.now().minusMinutes(50));

        when(this.taskTypeOverviewDataService.getByTaskIdentifier(anyString()))
                .thenReturn(taskTypeOverview);

        when(this.taskTypeEventDataService.getEventsSinceLastUpdateByTaskIdentifier(anyString(), any(LocalDateTime.class)))
                .thenReturn(List.of(taskTypeEvent));

        when(this.taskTypeEventDataService.getEventsByTaskIdentifier(anyString()))
                .thenReturn(List.of(oldTaskTypeEvent, taskTypeEvent));

        this.averageService.calculateNewAverage("A");

        verify(this.taskTypeOverviewDataService, times(1)).getByTaskIdentifier(anyString());
        verify(this.taskTypeEventDataService, times(1)).getEventsSinceLastUpdateByTaskIdentifier(taskIdentifierCaptor.capture(),lastUpdatedAtCaptor.capture());
        verify(this.taskTypeOverviewDataService, times(1)).updateAverageByTaskIdentifierType(updateTaskIdentifierCaptor.capture(),newAvgCaptor.capture());

        var taskIdentifier = taskIdentifierCaptor.getValue();
        assertEquals(taskTypeOverview.getTaskIdentifierType(), taskIdentifier);

        var lastUpdatedAt = lastUpdatedAtCaptor.getValue();
        assertEquals(taskTypeOverview.getUpdateAt(), lastUpdatedAt);

        var updateTaskIdentifier = updateTaskIdentifierCaptor.getValue();
        assertEquals(taskTypeOverview.getTaskIdentifierType(), updateTaskIdentifier);

        var newAvg = newAvgCaptor.getValue();
        assertEquals(6.0, newAvg);
    }

    @ParameterizedTest
    @MethodSource("calculateUpdatedAtDates")
    void getAverageDuration_whenOverAnHourSinceLastUpdateButNoNewEventsSinceLastUpdate_callCalculateAndUseCurrentAverage(LocalDateTime inputUpdatedAt) {
        var taskIdentifierCaptor = ArgumentCaptor.forClass(String.class);
        var lastUpdatedAtCaptor = ArgumentCaptor.forClass(LocalDateTime.class);

        var taskTypeOverview = new TaskTypeOverview();
        taskTypeOverview.setTaskIdentifierType("A");
        taskTypeOverview.setCurrentAverage(5.0);
        taskTypeOverview.setUpdatedAt(inputUpdatedAt);

        when(this.taskTypeOverviewDataService.getByTaskIdentifier(anyString()))
                .thenReturn(taskTypeOverview);

        when(this.taskTypeEventDataService.getEventsSinceLastUpdateByTaskIdentifier(anyString(), any(LocalDateTime.class)))
                .thenReturn(List.of());

        var result = this.averageService.getAverageDuration("A");
        assertEquals(5.0, result);

        verify(this.taskTypeOverviewDataService, times(1)).getByTaskIdentifier(anyString());
        verify(this.taskTypeEventDataService, times(1)).getEventsSinceLastUpdateByTaskIdentifier(taskIdentifierCaptor.capture(),lastUpdatedAtCaptor.capture());

        var taskIdentifier = taskIdentifierCaptor.getValue();
        assertEquals(taskTypeOverview.getTaskIdentifierType(), taskIdentifier);

        var lastUpdatedAt = lastUpdatedAtCaptor.getValue();
        assertEquals(taskTypeOverview.getUpdateAt(), lastUpdatedAt);
    }


    @Test
    void getAverageDuration_whenNotAnHourSinceLastUpdateButNoNewEventsSinceLastUpdate_doNotCallCalculateAndUseCurrentAverage() {
        var taskTypeOverview = new TaskTypeOverview();
        taskTypeOverview.setTaskIdentifierType("A");
        taskTypeOverview.setCurrentAverage(6.0);
        taskTypeOverview.setUpdatedAt(LocalDateTime.now().minusMinutes(5));

        when(this.taskTypeOverviewDataService.getByTaskIdentifier(anyString()))
                .thenReturn(taskTypeOverview);

        when(this.taskTypeEventDataService.getEventsSinceLastUpdateByTaskIdentifier(anyString(), any(LocalDateTime.class)))
                .thenReturn(List.of());

        var result = this.averageService.getAverageDuration("A");
        assertEquals(6.0, result);

        verify(this.taskTypeOverviewDataService, times(1)).getByTaskIdentifier(anyString());
        verify(this.taskTypeEventDataService, never()).getEventsSinceLastUpdateByTaskIdentifier(anyString(), any(LocalDateTime.class));
        verify(this.taskTypeEventDataService, never()).getEventsByTaskIdentifier(anyString());
    }

    @Test
    void getAverageDuration_whenAnHourSinceLastUpdateAndNewEventsSinceLastUpdate_callCalculateAndUseNewAverage() {
        var taskIdentifierCaptor = ArgumentCaptor.forClass(String.class);
        var lastUpdatedAtCaptor = ArgumentCaptor.forClass(LocalDateTime.class);

        var taskTypeOverview = new TaskTypeOverview();
        taskTypeOverview.setTaskIdentifierType("A");
        taskTypeOverview.setCurrentAverage(0.0);
        taskTypeOverview.setUpdatedAt(LocalDateTime.now().minusMinutes(65));

        var taskTypeEvent = new TaskTypeEvent();
        taskTypeEvent.setTaskIdentifierType("A");
        taskTypeEvent.setDuration(3.0);
        taskTypeEvent.setCreatedAt(LocalDateTime.now().minusMinutes(25));

        when(this.taskTypeOverviewDataService.getByTaskIdentifier(anyString()))
                .thenReturn(taskTypeOverview);

        when(this.taskTypeEventDataService.getEventsSinceLastUpdateByTaskIdentifier(anyString(), any(LocalDateTime.class)))
                .thenReturn(List.of(taskTypeEvent));

        when(this.taskTypeEventDataService.getEventsByTaskIdentifier(anyString()))
                .thenReturn(List.of(taskTypeEvent));

        var result = this.averageService.getAverageDuration("A");
        assertEquals(3.0, result);

        verify(this.taskTypeOverviewDataService, times(1)).getByTaskIdentifier(anyString());
        verify(this.taskTypeEventDataService, times(1)).getEventsSinceLastUpdateByTaskIdentifier(taskIdentifierCaptor.capture(),lastUpdatedAtCaptor.capture());
        verify(this.taskTypeOverviewDataService, times(1)).updateAverageByTaskIdentifierType(anyString(), anyDouble());

        var taskIdentifier = taskIdentifierCaptor.getValue();
        assertEquals(taskTypeOverview.getTaskIdentifierType(), taskIdentifier);

        var lastUpdatedAt = lastUpdatedAtCaptor.getValue();
        assertEquals(taskTypeOverview.getUpdateAt(), lastUpdatedAt);
    }

    private static Stream<LocalDateTime> calculateUpdatedAtDates() {
    return Stream.of(
            LocalDateTime.now().minusMinutes(60),
            LocalDateTime.of(2023,1,1,12,12,12,100)
    );
    }
}