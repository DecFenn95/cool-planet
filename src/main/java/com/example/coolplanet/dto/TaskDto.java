package com.example.coolplanet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TaskDto {
    private String taskIdentifier;
    private Double duration;
}

