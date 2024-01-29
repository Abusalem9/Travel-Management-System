package com.nymble.travelmanagementsystem.response;

import lombok.Data;

@Data
public class Task4Response {
    private String activityName;
    private String activityDescription;
    private Long activityCapacity;
    private Long consumedActivityCapacity;
    private Long availableActivityCapacity;
}
