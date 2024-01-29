package com.nymble.travelmanagementsystem.request;

import lombok.Data;


@Data
public class CreateBooking {
    private String activityId;
    private String travelPackageId;
    private String destinationId;
    private String passengerId;
    private String createdBy;
    private String createdByName;
}
