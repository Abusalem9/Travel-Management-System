package com.nymble.travelmanagementsystem.response;

import com.nymble.travelmanagementsystem.entity.DestinationEntity;
import lombok.Data;

import java.util.List;

@Data
public class Task1Response {
    private String travelPackageName;
    private List<Task1DestinationResponse> destinations;
}
