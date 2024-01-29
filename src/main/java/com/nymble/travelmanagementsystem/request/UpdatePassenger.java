package com.nymble.travelmanagementsystem.request;

import com.nymble.travelmanagementsystem.constants.PassengerType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdatePassenger {
    private String passengerId;
    private String passengerName;
    private String passengerNumber;
    private PassengerType passengerType;
    private BigDecimal passengerBalance;
    private String updatedBy;
    private String updatedByName;
}
