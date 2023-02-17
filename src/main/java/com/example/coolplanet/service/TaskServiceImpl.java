package com.example.coolplanet.service;

import com.example.coolplanet.response.TaskAverageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TaskServiceImpl implements TaskService {

    private final AverageService averageService;

    public TaskServiceImpl(AverageService averageService) {
        this.averageService = averageService;
    }

    @Override
    public boolean taskPerformed(String taskIdentifier, Double duration) {

        try {
            // this.taskRepository.save(taskIdentifier, duration, LocalDateTime.now())

             // if new taskIdentifier = create record in 2 tables (status table and overall table)
            this.averageService.calculateNewAverage(taskIdentifier);
        }
        catch (Exception e) {
            log.warn(String.format("Issue while adding task: %s", e.getMessage()));
            return false;
        }

        return true;
    }

    @Override
    public TaskAverageResponse currentAverageTime(String taskIdentifier) {
        var averageDuration = this.averageService.getAverageDuration(taskIdentifier);

        return TaskAverageResponse.builder().taskIdentifier(taskIdentifier).averageDuration(averageDuration).build();
    }
}
