package com.nymble.travelmanagementsystem.repository;

import com.nymble.travelmanagementsystem.entity.TravelPackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelPackageRepository extends JpaRepository<TravelPackageEntity, String> {
    TravelPackageEntity getTravelPackageEntityByTravelPackageId(String travelPackageId);
}
