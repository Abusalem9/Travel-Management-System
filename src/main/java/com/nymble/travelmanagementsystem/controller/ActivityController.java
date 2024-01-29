package com.nymble.travelmanagementsystem.controller;

import com.nymble.travelmanagementsystem.entity.ActivityEntity;
import com.nymble.travelmanagementsystem.request.ActivityFilterRequest;
import com.nymble.travelmanagementsystem.request.CreateActivityV1;
import com.nymble.travelmanagementsystem.request.UpdateActivity;
import com.nymble.travelmanagementsystem.response.BaseResponse;
import com.nymble.travelmanagementsystem.service.ActivityService;
import com.nymble.travelmanagementsystem.utils.BaseResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.directory.SearchResult;

@RestController
@RequestMapping("/activity")
@Slf4j
public class ActivityController {

    @Autowired
    ActivityService activityService;

    @Operation(summary = "create Activity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activity created successfully",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request data",
                    content = @Content(schema = @Schema(implementation = Exception.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createActivity(@RequestBody CreateActivityV1 createActivity) {
        try {
            log.info("Received a request to create Activity.");
            log.debug("Request data: {}", createActivity);
            BaseResponse savedActivity = activityService.createActivity(createActivity);
            log.info("Activity created successfully. Activity details: {}", savedActivity);
            return new ResponseEntity<>(savedActivity, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while creating Activity: {}", e.getMessage(), e);
            BaseResponse errorMessage = BaseResponseUtil.createErrorBaseResponse(e.getMessage());
            log.error("Error response sent: {}", errorMessage);
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "update Activity")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activity updated successfully",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request data",
                    content = @Content(schema = @Schema(implementation = Exception.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateActivity(@RequestBody UpdateActivity updateActivity) {
        try {
            log.info("Received a request to update Activity.");
            log.debug("Request data: {}", updateActivity);
            BaseResponse savedActivity = activityService.updateActivity(updateActivity);
            log.info("Activity updated successfully. Activity details: {}", savedActivity);
            return new ResponseEntity<>(savedActivity, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while creating Activity: {}", e.getMessage(), e);
            BaseResponse errorMessage = BaseResponseUtil.createErrorBaseResponse(e.getMessage());
            log.error("Error response sent: {}", errorMessage);
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Endpoint for filtering Activity's based on criteria.
     *
     * @param activityFilterRequest The request body containing filter criteria for Activity's.
     * @return ResponseEntity containing the filtered Activity's or an error response.
     */
    @Operation(summary = "Filter Activity's")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activity's filtered successfully",
                    content = @Content(schema = @Schema(implementation = SearchResult.class))),
            @ApiResponse(responseCode = "400", description = "Bad request data",
                    content = @Content(schema = @Schema(implementation = Exception.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping(value = "/filter", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity filterActivities(@RequestBody ActivityFilterRequest activityFilterRequest) {
        try {
            // Log the start of the Activity filtering process.
            log.info("Received a request to filter Activity's.");

            // Log information about the incoming filter criteria for documentation purposes.
            log.debug("Filter criteria: {}", activityFilterRequest);

            // Call the service to filter the Activity's.
            BaseResponse filteredActivities = activityService.filterActivity(activityFilterRequest);

            // Log information about the successful filtering for documentation purposes.
            log.info("Activity's filtered successfully. Filtered details: {}", filteredActivities);

            // Return a success response with the filtered Activity's.
            return new ResponseEntity<>(filteredActivities, HttpStatus.OK);
        } catch (Exception e) {
            // Log an error message if an exception occurs.
            log.error("Error occurred while filtering Activity's: {}", e.getMessage(), e);

            // Create an error response with the error message.
            BaseResponse errorMessage = BaseResponseUtil.createErrorBaseResponse(e.getMessage());

            // Log the error response for documentation purposes.
            log.error("Error response sent: {}", errorMessage);

            // Return an error response with the error message.
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint for retrieving information about a specific Activity based on its ID.
     *
     * @param activityId The ID of the Activity to retrieve.
     * @return ResponseEntity containing the requested Activity or an error response.
     */
    @Operation(summary = "Get a Activity by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activity retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ActivityEntity.class))),
            @ApiResponse(responseCode = "404", description = "Activity not found",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @GetMapping(value = "/{activityId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getActivityById(@PathVariable String activityId) {
        try {
            // Log the start of the Activity retrieval process.
            log.info("Received a request to retrieve a Activity by ID: {}", activityId);

            // Call the service to get the Activity by ID.
            BaseResponse retrievedActivity = activityService.getActivityById(activityId);

            if (retrievedActivity != null) {
                // Log information about the successful retrieval for documentation purposes.
                log.info("Activity retrieved successfully. Retrieved details: {}", retrievedActivity);

                // Return a success response with the retrieved Activity.
                return new ResponseEntity<>(retrievedActivity, HttpStatus.OK);
            } else {
                // Log a not found message if the Activity with the given ID is not found.
                log.warn("Activity not found with ID: {}", activityId);

                // Create a not found response.
                BaseResponse notFoundMessage = BaseResponseUtil.createErrorBaseResponse("Activity not found with ID: " + activityId);

                // Return a not found response.
                return new ResponseEntity<>(notFoundMessage, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            // Log an error message if an exception occurs.
            log.error("Error occurred while retrieving a Activity: {}", e.getMessage(), e);

            // Create an error response with the error message.
            BaseResponse errorMessage = BaseResponseUtil.createErrorBaseResponse(e.getMessage());

            // Log the error response for documentation purposes.
            log.error("Error response sent: {}", errorMessage);

            // Return an error response with the error message.
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
