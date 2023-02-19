package com.example.coolplanet.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "task_type_overview")
public class TaskTypeOverview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "task_identifier")
    //@Enumerated(EnumType.STRING)
    private String taskIdentifierType;

    @Column(name = "current_average")
    private Double currentAverage;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // getters and setters and constructor

    public TaskTypeOverview() {

    }

    public String getTaskIdentifierType() {
        return taskIdentifierType;
    }

    public void setTaskIdentifierType(String taskIdentifierType) {
        this.taskIdentifierType = taskIdentifierType;
    }

    public Double getCurrentAverage() {
        return currentAverage;
    }

    public void setCurrentAverage(Double currentAverage) {
        this.currentAverage = currentAverage;
    }

    public LocalDateTime getUpdateAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
