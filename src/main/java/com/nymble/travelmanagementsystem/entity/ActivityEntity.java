package com.nymble.travelmanagementsystem.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
@Table(name = "ACTIVITY", schema = DBConstants.SCHEMA)
@JsonPropertyOrder({
        "activityId",
        "destinationId",
        "activityName",
        "activityDescription",
        "activityCost",
        "activityCapacity",
        "consumedActivityCapacity",
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
public class ActivityEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DBConstants.ACTIVITY_SEQUENCE)
    @GenericGenerator(name = DBConstants.ACTIVITY_SEQUENCE, strategy = "com.nymble.travelmanagementsystem.sequence.TimeStampPrefixedSequenceIdGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = TimeStampPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "ACT")
            })
    @Column(name = "ACTIVITY_ID")
    private String activityId;
    @Column(name = "DESTINATION_ID")
    private String destinationId;
    @Column(name = "ACTIVITY_NAME")
    private String activityName;
    @Column(name = "ACTIVITY_DESCRIPTION")
    private String activityDescription;
    @Column(name = "ACTIVITY_COST")
    private BigDecimal activityCost;
    @Column(name = "ACTIVITY_CAPACITY")
    private Long activityCapacity;
    @Column(name = "CONSUMED_ACTIVITY_CAPACITY")
    private Long consumedActivityCapacity = 0L;
}