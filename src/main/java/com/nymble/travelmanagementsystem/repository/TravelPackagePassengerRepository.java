package com.nymble.travelmanagementsystem.repository;

import com.nymble.travelmanagementsystem.entity.TravelPackagePassengerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelPackagePassengerRepository extends JpaRepository<TravelPackagePassengerEntity, String> {
    TravelPackagePassengerEntity getTravelPackagePassengerEntityByPassengerIdAndTravelPackageId(String passengerId, String travelPackageId);
}
