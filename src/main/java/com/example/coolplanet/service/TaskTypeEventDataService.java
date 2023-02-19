package com.example.coolplanet.service;

import com.example.coolplanet.models.TaskTypeEvent;
import com.example.coolplanet.models.TaskTypeOverview;
import com.example.coolplanet.repository.TaskTypeEventRepository;
import com.example.coolplanet.repository.TaskTypeOverviewRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskTypeEventDataService {

    private final TaskTypeEventRepository taskTypeEventRepository;

    public TaskTypeEventDataService(TaskTypeEventRepository taskTypeEventRepository) {
        this.taskTypeEventRepository = taskTypeEventRepository;
    }

    public List<TaskTypeEvent> getEventsByTaskIdentifier(String taskIdentifier) {
        return this.taskTypeEventRepository.getEventsByTaskIdentifier(taskIdentifier);
    }

    public List<TaskTypeEvent> getEventsSinceLastUpdateByTaskIdentifier(String taskIdentifier, LocalDateTime lastUpdatedAt) {
        return this.taskTypeEventRepository.getEventsSinceLastUpdateByTaskIdentifier(taskIdentifier, lastUpdatedAt);
    }

    public List<TaskTypeEvent> getAll() {
        return this.taskTypeEventRepository.findAll();
    }
}
