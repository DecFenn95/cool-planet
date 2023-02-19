package com.example.coolplanet.service.data;

import com.example.coolplanet.entity.TaskTypeOverview;
import com.example.coolplanet.repository.TaskTypeOverviewRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TaskTypeOverviewDataService {

    private final TaskTypeOverviewRepository taskTypeOverviewRepository;

    public TaskTypeOverviewDataService(TaskTypeOverviewRepository taskTypeOverviewRepository) {
        this.taskTypeOverviewRepository = taskTypeOverviewRepository;
    }

    public TaskTypeOverview getByTaskIdentifier(String taskIdentifier) {
        return this.taskTypeOverviewRepository.getByTaskIdentifier(taskIdentifier);
    }

    public void updateAverageByTaskIdentifierType(String taskIdentifier, Double newAverage) {
        this.taskTypeOverviewRepository.updateAverageByTaskIdentifierType(newAverage, LocalDateTime.now(), taskIdentifier);
    }
}
