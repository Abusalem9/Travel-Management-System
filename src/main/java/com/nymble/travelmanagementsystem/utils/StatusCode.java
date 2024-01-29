package com.nymble.travelmanagementsystem.utils;

public enum StatusCode {
    // CUSTOM BUSINESS CODES 1xxxx

    // Base codes
    OK(200, 0, "Success"),
    SERVER_ERROR(400, 1, "Failed!"),
    SUCCESS(201, 0, "Success"),


    // Server & security error codes
    NO_DATA(300, 2, "No data found!"),
    INTERNAL_SERVER_ERROR(500, 1, "Sorry! Something went wrong."),
    BAD_REQUEST(400, 1, "Bad Request"),
    ACTIVITY_NOT_FOUND(2500, 0, "Activity not found for this Id."),
    ACTIVITIES_NOT_FOUND(2501, 0, "Activities not found for this Ids."),
    ACTIVITY_CAPACITY_FULL(1001, 0, "Activity capacity full"),
    BOOKING_COMPLETED(201, 0, "Booking Has been successfully completed."),
    PASSENGER_NOT_FOUND(2502, 0, "Passenger not found for this Id."),
    INSUFFICIENT_PASSENGER_BALANCE(2505, 0, "Insufficient passenger balance."),
    PASSENGER_CAPACITY_FULL(2506, 0, "Activity capacity full"),
    DESTINATION_NOT_FOUND(2509, 0, "Destination not found for this Id."),
    BOOKING_NOT_FOUND(2511, 0, "Booking not found for this Id.");


    private Integer code;
    private Integer errorState;
    private String statusMessage;

    StatusCode(Integer code, Integer errorState, String statusMessage) {
        this.code = code;
        this.statusMessage = statusMessage;
        this.errorState = errorState;
    }

    public StatusCode getByStatusCode(Integer code) {
        StatusCode[] values = values();
        for (StatusCode statusCode : values) {
            if (statusCode.getCode().equals(code)) {
                return statusCode;
            }
        }
        return StatusCode.SERVER_ERROR;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getStatusMessage() {
        return this.statusMessage;
    }

    public Integer getErrorState() {
        return errorState;
    }
}
