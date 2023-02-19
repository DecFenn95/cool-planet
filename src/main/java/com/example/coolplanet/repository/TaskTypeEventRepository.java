package com.example.coolplanet.repository;

import com.example.coolplanet.models.TaskTypeEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskTypeEventRepository extends JpaRepository<TaskTypeEvent, Long> {

    @Query(value = "SELECT * FROM task_type_event t WHERE t.task_identifier = ?1", nativeQuery = true)
    List<TaskTypeEvent> getEventsByTaskIdentifier(@Param("task_identifier") String taskIdentifier);

    @Query(value = "SELECT * FROM task_type_event t WHERE t.task_identifier = ?1 AND t.created_at >= ?2", nativeQuery = true)
    List<TaskTypeEvent> getEventsSinceLastUpdateByTaskIdentifier(@Param("task_identifier") String taskIdentifier, @Param("updated_at") LocalDateTime lastUpdatedAt);
}
