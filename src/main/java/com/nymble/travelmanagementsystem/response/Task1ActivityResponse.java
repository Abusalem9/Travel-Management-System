package com.nymble.travelmanagementsystem.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Task1ActivityResponse {
    private String activityName;
    private BigDecimal activityCost;
    private Long activityCapacity;
    private String activityDescription;
}
