package com.example.coolplanet.service;

public interface AverageService {
    void calculateNewAverage(String taskId);

    double getAverageDuration(String taskId);
}
