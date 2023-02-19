package com.example.coolplanet.service;

import com.example.coolplanet.dto.AverageRequestDto;
import com.example.coolplanet.dto.TaskRequestDto;
import com.example.coolplanet.entity.TaskTypeEvent;
import com.example.coolplanet.response.TaskAverageResponse;
import com.example.coolplanet.service.data.TaskTypeEventDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TaskServiceImpl implements TaskService {

    private final AverageService averageService;

    private final TaskTypeEventDataService taskTypeEventDataService;

    public TaskServiceImpl(AverageService averageService, TaskTypeEventDataService taskTypeEventDataService) {
        this.averageService = averageService;
        this.taskTypeEventDataService = taskTypeEventDataService;
    }

    @Override
    public boolean taskPerformed(TaskRequestDto taskRequestDto) {
        try {
            var taskTypeEvent = new TaskTypeEvent();
            taskTypeEvent.setTaskIdentifierType(taskRequestDto.getTaskIdentifier());
            taskTypeEvent.setDuration(taskRequestDto.getDuration());

            this.taskTypeEventDataService.createTaskEvent(taskTypeEvent);

            // if new taskIdentifier = create record in 2 tables (status table and overall table)
            this.averageService.calculateNewAverage(taskRequestDto.getTaskIdentifier());
        }
        catch (Exception e) {
            log.error("Issue while adding task event!");
            return false;
        }

        return true;
    }

    @Override
    public TaskAverageResponse currentAverageTime(AverageRequestDto averageRequestDto) {
        try {
            var averageDuration = this.averageService.getAverageDuration(averageRequestDto.getTaskIdentifier());

            return TaskAverageResponse.builder().taskIdentifier(averageRequestDto.getTaskIdentifier()).averageDuration(averageDuration).build();
        }
        catch (Exception e) {
            log.error("Issue while retrieving current average!");
            return TaskAverageResponse.builder().taskIdentifier(averageRequestDto.getTaskIdentifier()).averageDuration(0.0).build();
        }
    }
}
