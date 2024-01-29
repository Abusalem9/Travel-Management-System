package com.nymble.travelmanagementsystem.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.nymble.travelmanagementsystem.constants.DBConstants;
import com.nymble.travelmanagementsystem.sequence.TimeStampPrefixedSequenceIdGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TRAVEL_PACKAGE", schema = DBConstants.SCHEMA)
@JsonPropertyOrder({
        "travelPackageId",
        "travelPackageName",
        "passengerCapacity",
        "consumedPassengerCapacity",
        "created",
        "updated",
        "deleted",
        "active",
        "createdBy",
        "createdByName",
        "updatedBy",
        "updatedByName",
        "createdAt",
        "updatedAt",
        "destinations",
        "passengers",
})
public class TravelPackageEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DBConstants.TRAVEL_PACKAGE_SEQUENCE)
    @GenericGenerator(name = DBConstants.TRAVEL_PACKAGE_SEQUENCE, strategy = "com.nymble.travelmanagementsystem.sequence.TimeStampPrefixedSequenceIdGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = TimeStampPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "TP")
            })
    @Column(name = "TRAVEL_PACKAGE_ID")
    private String travelPackageId;
    @Column(name = "TRAVEL_PACKAGE_NAME")
    private String travelPackageName;
    @Column(name = "PASSENGER_CAPACITY")
    private Long passengerCapacity;
    @Column(name = "CONSUMED_PASSENGER_CAPACITY")
    private Long consumedPassengerCapacity = 0L;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "TRAVEL_PACKAGE_ID", referencedColumnName = "TRAVEL_PACKAGE_ID")
    private List<DestinationEntity> destinations;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "TRAVEL_PACKAGE_ID", referencedColumnName = "TRAVEL_PACKAGE_ID")
    private List<TravelPackagePassengerEntity> passengers;
}
