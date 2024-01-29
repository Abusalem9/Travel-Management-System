package com.nymble.travelmanagementsystem.service;

import com.nymble.travelmanagementsystem.request.CreateTravelPackage;
import com.nymble.travelmanagementsystem.request.TravelPackageFilterRequest;
import com.nymble.travelmanagementsystem.response.BaseResponse;
import com.nymble.travelmanagementsystem.response.TravelPackageResponse;

public interface TravelService {

    BaseResponse createTravelPackage(CreateTravelPackage createTravelPackage);

    BaseResponse filterTravelPackage(TravelPackageFilterRequest travelPackageFilterRequest);

    BaseResponse getTravelPackageById(String travelPackageId);
}
