package com.nymble.travelmanagementsystem.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateBooking {
    private String bookingId;
    private String activityId;
    private String travelPackageId;
    private String destinationId;
    private String passengerId;
    private BigDecimal bookingCost;
    private String bookingStatus;
    private String updatedBy;
    private String updatedByName;
}
