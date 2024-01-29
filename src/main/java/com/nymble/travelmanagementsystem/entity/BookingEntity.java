package com.nymble.travelmanagementsystem.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.nymble.travelmanagementsystem.constants.BookingStatus;
import com.nymble.travelmanagementsystem.constants.DBConstants;
import com.nymble.travelmanagementsystem.sequence.TimeStampPrefixedSequenceIdGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "BOOKING", schema = DBConstants.SCHEMA)
@JsonPropertyOrder({
        "bookingId",
        "activityId",
        "travelPackageId",
        "destinationId",
        "passengerId",
        "bookingCost",
        "bookingStatus",
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
})
public class BookingEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DBConstants.BOOKING_SEQUENCE)
    @GenericGenerator(name = DBConstants.BOOKING_SEQUENCE, strategy = "com.nymble.travelmanagementsystem.sequence.TimeStampPrefixedSequenceIdGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = TimeStampPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "OD")
            })
    @Column(name = "BOOKING_ID")
    private String bookingId;
    @Column(name = "ACTIVITY_ID")
    private String activityId;
    @Column(name = "TRAVEL_PACKAGE_ID")
    private String travelPackageId;
    @Column(name = "DESTINATION_ID")
    private String destinationId;
    @Column(name = "PASSENGER_ID")
    private String passengerId;
    @Column(name = "BOOKING_COST")
    private BigDecimal bookingCost;
    @Column(name = "BOOKING_STATUS")
    private String bookingStatus = BookingStatus.BOOKED.name();
}
