package com.example.coolplanet.service.data;

import com.example.coolplanet.entity.TaskTypeEvent;
import com.example.coolplanet.repository.TaskTypeEventRepository;
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

    public void createTaskEvent(TaskTypeEvent taskTypeEvent) {
        taskTypeEvent.setCreatedAt(LocalDateTime.now());
        this.taskTypeEventRepository.save(taskTypeEvent);
    }
}
