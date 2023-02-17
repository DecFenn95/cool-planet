package com.example.coolplanet.controller;

import com.example.coolplanet.request.AverageRequest;
import com.example.coolplanet.request.TaskRequest;
import com.example.coolplanet.response.TaskAverageResponse;
import com.example.coolplanet.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AverageControllerTest {

    private TaskService taskService;

    private AverageController averageController;

    @BeforeEach
    void setUp() {
        this.taskService = mock(TaskService.class);
        this.averageController = new AverageController(this.taskService);
    }

    @Test
    void taskPerformed_whenTaskRequestIsNull_returnBadRequest() {
        var result = this.averageController.taskPerformed(null);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @ParameterizedTest
    @MethodSource("invalidTaskRequests")
    void taskPerformed_whenTaskRequestIsInvalid_returnBadRequest(TaskRequest input) {
        var result = this.averageController.taskPerformed(input);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void taskPerformed_whenTaskRequestValid_returnSuccessfulRequest() {
        when(this.taskService.taskPerformed(anyString(), anyDouble())).thenReturn(true);

        var taskRequest = TaskRequest.builder().taskIdentifier("A").duration(10.0).build();
        var result = this.averageController.taskPerformed(taskRequest);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void currentAverageTime_whenAverageRequestIsNull_returnBadRequest() {
        var result = this.averageController.currentAverageTime(null);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @ParameterizedTest
    @MethodSource("invalidAverageRequests")
    void currentAverageTime_whenAverageRequestInvalid_returnBadRequest(AverageRequest input) {
        var result = this.averageController.currentAverageTime(input);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void currentAverageTime_whenAverageRequestValid_returnSuccessfulRequest() {
        var response = TaskAverageResponse.builder().taskIdentifier("A").averageDuration(5.0).build();
        when(this.taskService.currentAverageTime(anyString())).thenReturn(response);

        var averageRequest = AverageRequest.builder().taskIdentifier("A").build();
        var result = this.averageController.currentAverageTime(averageRequest);

        assertEquals(HttpStatus.OK, result.getStatusCode());

        var body = result.getBody();
        assertNotNull(body);
        assertEquals("A", body.getTaskIdentifier());
        assertEquals(5.0, body.getAverageDuration());
    }

    private static Stream<TaskRequest> invalidTaskRequests() {
        return Stream.of(
                TaskRequest.builder().build(),
                TaskRequest.builder().taskIdentifier("").build(),
                TaskRequest.builder().taskIdentifier("WRONG").build(),
                TaskRequest.builder().duration(10.0).build(),
                TaskRequest.builder().taskIdentifier("").duration(10.0).build(),
                TaskRequest.builder().taskIdentifier("WRONG").duration(10.0).build(),
                TaskRequest.builder().taskIdentifier("A").build(),
                TaskRequest.builder().taskIdentifier("A").duration(0.0).build(),
                TaskRequest.builder().taskIdentifier("A").duration(-0.1).build(),
                TaskRequest.builder().taskIdentifier("A").duration(-5.5).build()
        );
    }

    private static Stream<AverageRequest> invalidAverageRequests() {
        return Stream.of(
                AverageRequest.builder().build(),
                AverageRequest.builder().taskIdentifier("").build(),
                AverageRequest.builder().taskIdentifier("WRONG").build()
        );
    }
}