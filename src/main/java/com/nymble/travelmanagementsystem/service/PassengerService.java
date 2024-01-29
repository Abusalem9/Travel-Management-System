package com.nymble.travelmanagementsystem.service;

import com.nymble.travelmanagementsystem.request.CreatePassengerV1;
import com.nymble.travelmanagementsystem.request.PassengerFilterRequest;
import com.nymble.travelmanagementsystem.request.UpdatePassenger;
import com.nymble.travelmanagementsystem.response.BaseResponse;

public interface PassengerService {
    BaseResponse createPassenger(CreatePassengerV1 createPassenger);

    BaseResponse updatePassenger(UpdatePassenger updatePassenger);

    BaseResponse filterPassenger(PassengerFilterRequest passengerFilterRequest);

    BaseResponse getPassengerById(String passengerId);
}
