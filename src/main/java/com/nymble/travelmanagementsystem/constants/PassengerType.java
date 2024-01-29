package com.nymble.travelmanagementsystem.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum PassengerType {
    STANDARD(0),
    GOLD(1),
    PREMIUM(2);

    private final int typeValue;

    PassengerType(int typeVal) {
        this.typeValue = typeVal;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static PassengerType getPassengerType(final String type) {
        PassengerType passengerType = null;
        if (type != null) {
            for (PassengerType passengerTypeRetrieved : PassengerType.values()) {
                if (passengerTypeRetrieved.toString().equalsIgnoreCase(type)) {
                    passengerType = passengerTypeRetrieved;
                    break;
                }
            }
        }
        return passengerType;
    }
}
