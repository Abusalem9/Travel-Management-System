package com.nymble.travelmanagementsystem.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateActivityV1 {
    private String destinationId;
    private String activityName;
    private String activityDescription;
    private BigDecimal activityCost;
    private Long activityCapacity;
    private String createdBy;
    private String createdByName;
}
