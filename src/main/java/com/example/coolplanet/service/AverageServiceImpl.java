package com.example.coolplanet.service;

import com.example.coolplanet.entity.TaskTypeEvent;
import com.example.coolplanet.entity.TaskTypeOverview;
import com.example.coolplanet.service.data.TaskTypeEventDataService;
import com.example.coolplanet.service.data.TaskTypeOverviewDataService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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
        var requestTaskType = this.taskTypeOverviewDataService.getByTaskIdentifier(taskIdentifier);
        calculateRollingAverage(requestTaskType);
    }

    @Override
    public double getAverageDuration(String taskIdentifier) {
        var requestTaskType = this.taskTypeOverviewDataService.getByTaskIdentifier(taskIdentifier);

        var average = requestTaskType.getCurrentAverage();
        var difference = getTimeSinceLastUpdate(requestTaskType.getUpdateAt());

        // Arbitrary check to see if average hasn't been updated in an hour - recalculate it (to keep it up to date)
        if(difference >= 60) {
            average = calculateRollingAverage(requestTaskType);
        }

        return average;
    }

    private double calculateRollingAverage(TaskTypeOverview taskTypeOverview) {
        var taskIdentifier = taskTypeOverview.getTaskIdentifierType();
        var lastUpdatedAt = taskTypeOverview.getUpdateAt();

        var eventsSinceLastUpdate = this.taskTypeEventDataService.
                getEventsSinceLastUpdateByTaskIdentifier(taskIdentifier, lastUpdatedAt);

        // If no new events since the last request for the average return old average for taskIdentifier
        if(eventsSinceLastUpdate.isEmpty()) {
            return taskTypeOverview.getCurrentAverage();
        }

        var totalTaskCount = this.taskTypeEventDataService.getEventsByTaskIdentifier(taskIdentifier).size();
        var oldAverage = taskTypeOverview.getCurrentAverage();

        var newAverage = oldAverage * (totalTaskCount - eventsSinceLastUpdate.size()) / totalTaskCount +
                (eventsSinceLastUpdate.stream().map(TaskTypeEvent::getDuration).reduce(0.0, Double::sum) / totalTaskCount);

        this.taskTypeOverviewDataService.updateAverageByTaskIdentifierType(taskTypeOverview.getTaskIdentifierType(), newAverage);

        return newAverage;
    }

    private long getTimeSinceLastUpdate(LocalDateTime lastUpdateAt) {
        return LocalDateTime.from(lastUpdateAt).until(LocalDateTime.now(), ChronoUnit.MINUTES);
    }
}
