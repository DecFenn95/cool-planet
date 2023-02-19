package com.example.coolplanet.service;

import com.example.coolplanet.dto.TaskDto;
import com.example.coolplanet.models.TaskTypeEvent;
import com.example.coolplanet.models.TaskTypeOverview;
import com.example.coolplanet.repository.TaskTypeOverviewRepository;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AverageServiceImpl implements AverageService {

    private final TaskTypeOverviewDataService taskTypeOverviewDataService;
    private final TaskTypeEventDataService taskTypeEventDataService;

    public AverageServiceImpl(TaskTypeOverviewDataService taskTypeOverviewDataService, TaskTypeEventDataService taskTypeEventDataService) {
        this.taskTypeOverviewDataService = taskTypeOverviewDataService;
        this.taskTypeEventDataService = taskTypeEventDataService;
    }


    @Override
    public void calculateNewAverage(String taskIdentifier) {
        // every time this method is called - recalculate the new average for this task
        var requestTaskType = this.taskTypeOverviewDataService.getByTaskIdentifier(taskIdentifier.toUpperCase());
        calculateRollingAverage(requestTaskType);
    }

    @Override
    public double getAverageDuration(String taskIdentifier) {
        var requestTaskType = this.taskTypeOverviewDataService.getByTaskIdentifier(taskIdentifier.toUpperCase());

        var average = requestTaskType.getCurrentAverage();
        var difference = getTimeSinceLastUpdate(requestTaskType.getUpdateAt());

        // Arbitrary check of if average hasn't been updated in an hour - recalculate it
        if(difference >= 60) {
            average = calculateRollingAverage(requestTaskType);
        }

        return average;
    }

    private double calculateRollingAverage(TaskTypeOverview taskTypeOverview) {
        var taskIdentifier = taskTypeOverview.getTaskIdentifierType();
        var lastUpdatedAt = taskTypeOverview.getUpdateAt();

        var eventsSinceLastUpdate = this.taskTypeEventDataService.
                getEventsSinceLastUpdateByTaskIdentifier(taskIdentifier.toUpperCase(), lastUpdatedAt);

        // If no new events since the last request for the average return old average for taskIdentifier
        if(eventsSinceLastUpdate.isEmpty()) {
            return taskTypeOverview.getCurrentAverage();
        }

        var oldTotalTaskCount = this.taskTypeEventDataService.getEventsByTaskIdentifier(taskIdentifier.toUpperCase()).size();
        var oldAverage = taskTypeOverview.getCurrentAverage();

        var totalTaskCount = oldTotalTaskCount + eventsSinceLastUpdate.size();

        var newAverage = oldAverage * (totalTaskCount - eventsSinceLastUpdate.size())/totalTaskCount +
                (eventsSinceLastUpdate.stream().map(TaskTypeEvent::getDuration).reduce(0.0, Double::sum) / totalTaskCount);

        // update the db and save new average for specific task type identifier with current datetime
        //test.update(newAverage);

        this.taskTypeOverviewDataService.updateAverageByTaskIdentifierType(taskTypeOverview.getTaskIdentifierType(), newAverage);

        return newAverage;
    }

    private long getTimeSinceLastUpdate(LocalDateTime lastUpdateAt) {
        return LocalDateTime.from(lastUpdateAt).until(LocalDateTime.now(), ChronoUnit.MINUTES);
    }
}
