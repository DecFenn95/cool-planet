package com.example.coolplanet.repository;

import com.example.coolplanet.entity.TaskTypeOverview;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface TaskTypeOverviewRepository extends JpaRepository<TaskTypeOverview, Long> {

    @Query(value = "SELECT * FROM task_type_overview t WHERE t.task_identifier = ?1", nativeQuery = true)
    TaskTypeOverview getByTaskIdentifier(@Param("task_identifier") String taskIdentifier);

    @Transactional
    @Modifying
    @Query(value = "UPDATE task_type_overview SET current_average = ?1, updated_at = ?2 WHERE task_identifier = ?3", nativeQuery = true)
    void updateAverageByTaskIdentifierType(@Param("current_average") Double currentAverage, @Param("updated_at") LocalDateTime updatedAt, @Param("task_identifier") String taskIdentifier);
}
