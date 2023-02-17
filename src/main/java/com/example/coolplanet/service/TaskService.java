package com.example.coolplanet.service;

import com.example.coolplanet.response.TaskAverageResponse;

public interface TaskService {

    boolean taskPerformed(String taskId, Double average);

    TaskAverageResponse currentAverageTime(String taskId);
}
