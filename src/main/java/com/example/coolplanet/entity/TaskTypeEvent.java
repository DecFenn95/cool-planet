package com.example.coolplanet.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "task_type_event")
public class TaskTypeEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "task_identifier")
    private String taskIdentifierType;

    @Column(name = "duration")
    private Double duration;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public TaskTypeEvent() { }

    public String getTaskIdentifierType() {
        return taskIdentifierType;
    }

    public void setTaskIdentifierType(String taskIdentifierType) {
        this.taskIdentifierType = taskIdentifierType;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
