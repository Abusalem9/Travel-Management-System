package com.nymble.travelmanagementsystem.repository;

import com.nymble.travelmanagementsystem.entity.PassengerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerRepository extends JpaRepository<PassengerEntity, String> {
    PassengerEntity getPassengerEntityByPassengerIdOrPassengerNumber(String passengerId, String passengerNumber);
    PassengerEntity getPassengerEntityByPassengerId(String passengerId);
}
