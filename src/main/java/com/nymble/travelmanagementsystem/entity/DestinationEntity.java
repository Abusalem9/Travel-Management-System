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
@Table(name = "DESTINATION", schema = DBConstants.SCHEMA)
@JsonPropertyOrder({
        "destinationId",
        "travelPackageId",
        "destinationName",
        "destinationDescription",
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
        "activities"
})
public class DestinationEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DBConstants.DESTINATION_SEQUENCE)
    @GenericGenerator(name = DBConstants.DESTINATION_SEQUENCE, strategy = "com.nymble.travelmanagementsystem.sequence.TimeStampPrefixedSequenceIdGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = TimeStampPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "DES")
            })
    @Column(name = "DESTINATION_ID")
    private String destinationId;
    @Column(name = "TRAVEL_PACKAGE_ID")
    private String travelPackageId;
    @Column(name = "DESTINATION_NAME")
    private String destinationName;
    @Column(name = "DESTINATION_DESCRIPTION")
    private String destinationDescription;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "DESTINATION_ID", referencedColumnName = "DESTINATION_ID")
    private List<ActivityEntity> activities;
}
