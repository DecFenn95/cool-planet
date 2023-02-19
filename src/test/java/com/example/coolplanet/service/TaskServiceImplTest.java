package com.example.coolplanet.service;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.example.coolplanet.dto.AverageRequestDto;
import com.example.coolplanet.dto.TaskRequestDto;
import com.example.coolplanet.entity.TaskTypeEvent;
import com.example.coolplanet.service.data.TaskTypeEventDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TaskServiceImplTest {

    private AverageService averageService;

    private TaskTypeEventDataService taskTypeEventDataService;

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        this.averageService = mock(AverageService.class);
        this.taskTypeEventDataService = mock(TaskTypeEventDataService.class);
        this.taskService = new TaskServiceImpl(this.averageService, this.taskTypeEventDataService);
    }

    @Test
    void taskPerformed_whenUncheckedExceptionThrown_logAndReturnFalse() {
        var logger = (Logger) LoggerFactory.getLogger(TaskServiceImpl.class);
        var listAppender = new ListAppender<ILoggingEvent>();
        listAppender.start();
        logger.addAppender(listAppender);

        doThrow(NullPointerException.class).when(this.taskTypeEventDataService).createTaskEvent(any(TaskTypeEvent.class));

        var result = this.taskService.taskPerformed(TaskRequestDto.builder().build());
        assertFalse(result);

        var log = listAppender.list.get(0);
        assertEquals(Level.ERROR, log.getLevel());
        assertEquals("Issue while adding task event!", log.getMessage());
    }

    @Test
    void taskPerformed_whenValidDtoPassed_returnTrue() {
        var taskTypeEventCaptor = ArgumentCaptor.forClass(TaskTypeEvent.class);
        var taskIdentifierCaptor = ArgumentCaptor.forClass(String.class);

        var input = TaskRequestDto.builder().taskIdentifier("A").duration(6.0).build();

        var result = this.taskService.taskPerformed(input);

        verify(this.taskTypeEventDataService).createTaskEvent(taskTypeEventCaptor.capture());
        verify(this.averageService).calculateNewAverage(taskIdentifierCaptor.capture());

        assertTrue(result);

        var taskTypeEvent = taskTypeEventCaptor.getValue();
        assertEquals(input.getDuration(), taskTypeEvent.getDuration());
        assertEquals(input.getTaskIdentifier(), taskTypeEvent.getTaskIdentifierType());

        var taskIdentifier = taskIdentifierCaptor.getValue();
        assertEquals(input.getTaskIdentifier(), taskIdentifier);
    }

    @Test
    void currentAverageTime_whenUncheckedExceptionThrown_logAndReturnEmptyResponse() {
        var logger = (Logger) LoggerFactory.getLogger(TaskServiceImpl.class);
        var listAppender = new ListAppender<ILoggingEvent>();
        listAppender.start();
        logger.addAppender(listAppender);

        doThrow(NullPointerException.class).when(this.averageService).getAverageDuration(anyString());

        var result = this.taskService.currentAverageTime(AverageRequestDto.builder().taskIdentifier("A").build());
        assertEquals("A", result.getTaskIdentifier());
        assertEquals(0.0, result.getAverageDuration());

        var log = listAppender.list.get(0);
        assertEquals(Level.ERROR, log.getLevel());
        assertEquals("Issue while retrieving current average!", log.getMessage());
    }

    @Test
    void currentAverageTime_whenValidDtoPassed_returnCurrentAverage() {
        var taskIdentifierCaptor = ArgumentCaptor.forClass(String.class);
        var input = AverageRequestDto.builder().taskIdentifier("A").build();

        when(this.averageService.getAverageDuration(anyString())).thenReturn(10.5);

        var result = this.taskService.currentAverageTime(input);

        verify(this.averageService).getAverageDuration(taskIdentifierCaptor.capture());

        assertEquals("A", result.getTaskIdentifier());
        assertEquals(10.5, result.getAverageDuration());

        var taskIdentifier = taskIdentifierCaptor.getValue();
        assertEquals(input.getTaskIdentifier(), taskIdentifier);
    }
}