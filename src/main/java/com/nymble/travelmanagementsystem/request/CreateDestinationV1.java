package com.nymble.travelmanagementsystem.request;

import lombok.Data;

import java.util.List;

@Data
public class CreateDestinationV1 {
    private String destinationName;
    private String destinationDescription;
    private String createdBy;
    private String createdByName;
    private List<CreateActivity> createActivities;
}
