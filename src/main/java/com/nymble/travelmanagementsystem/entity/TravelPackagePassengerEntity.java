package com.nymble.travelmanagementsystem.entity;

import com.nymble.travelmanagementsystem.constants.DBConstants;
import com.nymble.travelmanagementsystem.sequence.TimeStampPrefixedSequenceIdGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TRAVEL_PACKAGE_PASSENGER", schema = DBConstants.SCHEMA)
public class TravelPackagePassengerEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DBConstants.TRAVEL_PACKAGE_PASSENGER_SEQUENCE)
    @GenericGenerator(name = DBConstants.TRAVEL_PACKAGE_PASSENGER_SEQUENCE, strategy = "com.nymble.travelmanagementsystem.sequence.TimeStampPrefixedSequenceIdGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = TimeStampPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "TPP")
            })
    @Column(name = "TRAVEL_PACKAGE_PASSENGER_ID")
    private String travelPackagePassengerId;
    @Column(name = "TRAVEL_PACKAGE_ID", unique = true)
    private String travelPackageId;
    @Column(name = "PASSENGER_ID", unique = true)
    private String passengerId;
}
