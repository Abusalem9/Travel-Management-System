package com.nymble.travelmanagementsystem.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Task3ActivityResponse {
    private String destinationId;
    private String destinationName;
    private String activityId;
    private String activityName;
    private String activityDescription;
    private BigDecimal bookingCost;
}
