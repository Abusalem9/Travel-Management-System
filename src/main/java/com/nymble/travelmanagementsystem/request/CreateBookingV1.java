package com.nymble.travelmanagementsystem.request;

import lombok.Data;

import java.util.List;


@Data
public class CreateBookingV1 {
    private List<String> activityIds;
    private String travelPackageId;
    private String destinationId;
    private String passengerId;
}
