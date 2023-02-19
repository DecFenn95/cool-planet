package com.example.coolplanet.helper;

import com.example.coolplanet.TaskIdentifierType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class RequestValidationHelper {

    public static boolean taskRequestInvalid(String taskIdentifier, Double duration) {
        return taskIdentifierInvalid(taskIdentifier) || duration == null || duration <= 0.0;
    }

    public static boolean averageRequestInvalid(String taskIdentifier) {
        return taskIdentifierInvalid(taskIdentifier);
    }

    private static boolean taskIdentifierInvalid(String taskIdentifier) {
        return taskIdentifier == null || taskIdentifier.isBlank() || taskIdentifierInvalidType(taskIdentifier);
    }

    private static boolean taskIdentifierInvalidType(String taskIdentifier) {
        for (var type: TaskIdentifierType.values()) {
            if(type.name().equals(taskIdentifier.toUpperCase())) {
                return false;
            }
        }
        return true;
    }
}
