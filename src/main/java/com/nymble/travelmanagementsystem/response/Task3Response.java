package com.nymble.travelmanagementsystem.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class Task3Response {
    private String passengerId;
    private String passengerName;
    private String passengerNumber;
    private BigDecimal passengerBalance;
    private List<Task3ActivityResponse> activities;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task3Response that)) return false;

        return getPassengerNumber().equals(that.getPassengerNumber());
    }

    @Override
    public int hashCode() {
        return getPassengerNumber().hashCode();
    }
}
