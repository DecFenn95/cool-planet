package com.example.coolplanet.service;

import com.example.coolplanet.dto.AverageRequestDto;
import com.example.coolplanet.dto.TaskRequestDto;
import com.example.coolplanet.response.TaskAverageResponse;

public interface TaskService {

    boolean taskPerformed(TaskRequestDto taskRequestDto);

    TaskAverageResponse currentAverageTime(AverageRequestDto averageRequestDto);
}
