package com.nymble.travelmanagementsystem.mapper;

import com.nymble.travelmanagementsystem.entity.*;
import com.nymble.travelmanagementsystem.request.*;
import com.nymble.travelmanagementsystem.response.Task1DestinationResponse;
import com.nymble.travelmanagementsystem.response.Task1Response;
import com.nymble.travelmanagementsystem.response.Task2Response;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface TravelPackageMapper {
    @Mapping(source = "createDestinations", target = "destinations")
    @Mapping(source = "createPassengers", target = "passengers")
    TravelPackageEntity createTravelPackageModelToTravelPackageEntity(CreateTravelPackage createTravelPackage);

    List<ActivityEntity> createActivityModelListToAcitvityEntityList(List<CreateActivity> createActivities);

    @Mapping(source = "createActivities", target = "activities")
    DestinationEntity mapToDestinationEntity(CreateDestination createDestination);

    @Mapping(source = "createActivities", target = "activities")
    List<DestinationEntity> createdDestinationModelListToCreateDestinationEntityList(List<CreateDestination> createDestinations);

    @Mapping(source = "activities", target = "activities")
    Task1DestinationResponse mapToTask1DestinationResponse(DestinationEntity createDestination);

    List<Task1DestinationResponse> destinationEntityListToTask1DestinationResponseList(List<DestinationEntity> destinationEntities);

    BookingEntity createBookingModelToBookingEntity(CreateBooking createBooking);

    PassengerEntity createPassengerModelToPassengerEntity(CreatePassengerV1 createPassenger);

    Task1Response travelPackageEntityToTask1Response(TravelPackageEntity travelPackageEntity);

    Task2Response travelPackageEntityToTask2Response(TravelPackageEntity travelPackageEntity);

    ActivityEntity createActivityV1ModelToActivityEntity(CreateActivityV1 createActivity);

}
