package com.nymble.travelmanagementsystem.repository;

import com.nymble.travelmanagementsystem.entity.DestinationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DestinationRepository extends JpaRepository<DestinationEntity, String> {
    DestinationEntity getDestinationEntityByDestinationId(String destinationId);
}
