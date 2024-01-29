package com.nymble.travelmanagementsystem.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateActivity {
    private String destinationId;
    private String activityId;
    private String activityName;
    private String activityDescription;
    private BigDecimal activityCost;
    private Long activityCapacity;
    private Long consumedActivityCapacity;
    private String updatedBy;
    private String updatedByName;
}
