package com.nymble.travelmanagementsystem.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum BookingStatus {
    BOOKED(0),
    CANCELLED(1);

    private final int statusValue;

    BookingStatus(int typeVal) {
        this.statusValue = typeVal;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static BookingStatus getPassengerType(final String type) {
        BookingStatus bookingStatus = null;
        if (type != null) {
            for (BookingStatus bookingStatusRetrieved : BookingStatus.values()) {
                if (bookingStatusRetrieved.toString().equalsIgnoreCase(type)) {
                    bookingStatus = bookingStatusRetrieved;
                    break;
                }
            }
        }
        return bookingStatus;
    }
}
