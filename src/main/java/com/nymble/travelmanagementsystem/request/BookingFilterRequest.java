package com.nymble.travelmanagementsystem.request;

import lombok.Data;

import javax.swing.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BookingFilterRequest {
    private Integer page = 1;
    private Integer pageSize = 20;
    private List<String> bookingId;
    private List<String> activityId;
    private List<String> travelPackageId;
    private List<String> destinationId;
    private List<String> passengerId;
    private BigDecimal bookingCost;
    private List<String> bookingStatus;
    private List<String> createdBy;
    private List<String> createdByName;
    private List<String> updatedBy;
    private List<String> updatedByName;
    private List<Boolean> deleted;
    private List<Boolean> active;
    private LocalDateTime createdStart;
    private LocalDateTime createdEnd;
    private LocalDateTime updatedStart;
    private LocalDateTime updatedEnd;
    private SortOrder sortByCreatedDate = SortOrder.UNSORTED;
    private SortOrder sortByUpdatedDate = SortOrder.UNSORTED;
}
