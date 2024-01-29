package com.nymble.travelmanagementsystem.request;

import lombok.Data;

import java.util.List;
@Data
public class CreateTravelPackage {
    private String travelPackageName;
    private Long passengerCapacity;
    private String createdBy;
    private String createdByName;
    List<CreateDestination> createDestinations;
    List<CreatePassenger> createPassengers;
}
