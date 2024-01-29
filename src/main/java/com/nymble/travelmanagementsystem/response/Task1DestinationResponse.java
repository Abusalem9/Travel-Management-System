package com.nymble.travelmanagementsystem.response;

import lombok.Data;

import java.util.List;

@Data
public class Task1DestinationResponse {
    private String destinationName;
    private String destinationDescription;
    private List<Task1ActivityResponse> activities;
}
