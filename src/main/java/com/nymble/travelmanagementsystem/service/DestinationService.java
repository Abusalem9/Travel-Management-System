package com.nymble.travelmanagementsystem.service;

import com.nymble.travelmanagementsystem.request.CreateDestination;
import com.nymble.travelmanagementsystem.request.DestinationFilterRequest;
import com.nymble.travelmanagementsystem.request.UpdateDestination;
import com.nymble.travelmanagementsystem.response.BaseResponse;

public interface DestinationService {
    BaseResponse updateDestination(UpdateDestination updateDestination);

    BaseResponse createDestination(CreateDestination createDestination);

    BaseResponse filterDestination(DestinationFilterRequest destinationFilterRequest);

    BaseResponse getDestinationById(String destinationId);
}
