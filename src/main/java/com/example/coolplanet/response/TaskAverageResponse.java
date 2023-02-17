package com.example.coolplanet.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TaskAverageResponse {
    private String taskIdentifier;
    private Double averageDuration;
}
