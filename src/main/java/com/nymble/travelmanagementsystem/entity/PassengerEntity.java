package com.nymble.travelmanagementsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.nymble.travelmanagementsystem.constants.DBConstants;
import com.nymble.travelmanagementsystem.sequence.TimeStampPrefixedSequenceIdGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PASSENGER", schema = DBConstants.SCHEMA)
@JsonPropertyOrder({
        "passengerId",
        "travelPackageId",
        "passengerName",
        "passengerNumber",
        "passengerType",
        "passengerBalance",
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
public class PassengerEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DBConstants.PASSENGER_SEQUENCE)
    @GenericGenerator(name = DBConstants.PASSENGER_SEQUENCE, strategy = "com.nymble.travelmanagementsystem.sequence.TimeStampPrefixedSequenceIdGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = TimeStampPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "P")
            })
    @Column(name = "PASSENGER_ID")
    private String passengerId;
    @Column(name = "PASSANGER_NAME")
    private String passengerName;
    @Column(name = "PASSANGER_NUMBER", unique = true)
    private String passengerNumber;
    @Column(name = "PASSENGER_TYPE")
    private String passengerType;
    @Column(name = "PASSENGER_BALANCE")
    private BigDecimal passengerBalance;

    @OneToMany
    @JsonIgnore
    @JoinColumn(name = "PASSENGER_ID", referencedColumnName = "PASSENGER_ID")
    private List<TravelPackagePassengerEntity> travelPackages;
}