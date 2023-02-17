package com.example.coolplanet.service;

import com.example.coolplanet.dto.TaskDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AverageServiceImpl implements AverageService {

    @Override
    public void calculateNewAverage(String taskIdentifier) {
        // every time this method is called - recalculate the new average for this task
        calculateRollingAverage(taskIdentifier);
    }

    @Override
    public double getAverageDuration(String taskIdentifier) {

        // check h2 for last time average was calculated - if longer than 1 hour (else return h2 average)
        // try to recalculate it with calculateRollingAverage
        // this will only recalculate if events are present after the time of the last calculation
        // there might be cases where events get submitted during calculation so this catches them

        // var calculatedRecently = checkLastProcessTime();

        // if(calculatedRecently)
        // {
        //    return h2 average for taskIdentifier
        // }
        // else
        // {
        //    return calculateRollingAverage(taskIdentifier);
        // }

        return 0.0;
    }

    private double calculateRollingAverage(String taskIdentifier) {
        //var lastUpdateDateTime = LocalDateTime.now();

        // Get count of all records from db table since lastUpdateDateTime
        var newTaskEvents = new ArrayList<TaskDto>();
        newTaskEvents.add(TaskDto.builder().TaskIdentifier("A").duration(2.0).build());
        newTaskEvents.add(TaskDto.builder().TaskIdentifier("A").duration(4.0).build());

        // If no new events since the last request for the average return old average for taskIdentifier
        //if(newTaskEvents.isEmpty()) {
        //    return oldAverage;
        //}

        var oldTotalTaskCount = 2; // get old total task count from db table
        var oldAverage = 2;

        var totalTaskCount = oldTotalTaskCount + newTaskEvents.size();

        var newAverage = oldAverage * (totalTaskCount - newTaskEvents.size())/totalTaskCount +
                (newTaskEvents.stream().map(TaskDto::getDuration).reduce(0.0, Double::sum) / totalTaskCount);

        // update the db and save new average for specific task type identifier with current datetime
        //test.update(newAverage);

        return newAverage;
    }
}
