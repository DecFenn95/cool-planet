package com.example.coolplanet.controller;

import com.example.coolplanet.dto.AverageRequestDto;
import com.example.coolplanet.dto.TaskRequestDto;
import com.example.coolplanet.request.AverageRequest;
import com.example.coolplanet.request.TaskRequest;
import com.example.coolplanet.response.TaskAverageResponse;
import com.example.coolplanet.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.coolplanet.helper.RequestValidationHelper.averageRequestInvalid;
import static com.example.coolplanet.helper.RequestValidationHelper.taskRequestInvalid;

@RestController
@RequestMapping("/")
public class AverageController {

    private final TaskService taskService;

    public AverageController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/taskPerformed")
    public ResponseEntity<Boolean> taskPerformed(@RequestBody TaskRequest taskRequest) {

        if(taskRequest == null || taskRequestInvalid(taskRequest.getTaskIdentifier(), taskRequest.getDuration())) {
            return ResponseEntity.badRequest().build();
        }

        var taskRequestDto = TaskRequestDto.builder()
                .taskIdentifier(taskRequest.getTaskIdentifier().toUpperCase())
                .duration(taskRequest.getDuration()).build();

        var result = this.taskService.taskPerformed(taskRequestDto);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/currentAverageTime")
    public ResponseEntity<TaskAverageResponse> currentAverageTime(@RequestBody AverageRequest averageRequest) {

        if(averageRequest == null || averageRequestInvalid(averageRequest.getTaskIdentifier())) {
            return ResponseEntity.badRequest().build();
        }

        var averageRequestDto = AverageRequestDto.builder()
                .taskIdentifier(averageRequest.getTaskIdentifier()).build();

        var result = this.taskService.currentAverageTime(averageRequestDto);

        return ResponseEntity.ok(result);
    }
}
