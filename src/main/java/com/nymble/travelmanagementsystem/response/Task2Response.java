package com.nymble.travelmanagementsystem.response;

import lombok.Data;

import java.util.List;

@Data
public class Task2Response {
    private String travelPackageName;
    private Long passengerCapacity;
    private Long consumedPassengerCapacity;
    private List<Task2PassengerResponse> passengers;
}
