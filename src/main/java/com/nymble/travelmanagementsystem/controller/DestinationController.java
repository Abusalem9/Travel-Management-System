package com.nymble.travelmanagementsystem.controller;

import com.nymble.travelmanagementsystem.entity.DestinationEntity;
import com.nymble.travelmanagementsystem.request.CreateDestination;
import com.nymble.travelmanagementsystem.request.DestinationFilterRequest;
import com.nymble.travelmanagementsystem.request.UpdateDestination;
import com.nymble.travelmanagementsystem.response.BaseResponse;
import com.nymble.travelmanagementsystem.service.DestinationService;
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
@RequestMapping("/destination")
@Slf4j
public class DestinationController {

    @Autowired
    DestinationService destinationService;

    @Operation(summary = "create Destination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Destination created successfully",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request data",
                    content = @Content(schema = @Schema(implementation = Exception.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createDestination(@RequestBody CreateDestination createDestination) {
        try {
            log.info("Received a request to create Destination.");
            log.debug("Request data: {}", createDestination);
            BaseResponse savedDestination = destinationService.createDestination(createDestination);
            log.info("Destination created successfully. Destination details: {}", savedDestination);
            return new ResponseEntity<>(savedDestination, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while creating Destination: {}", e.getMessage(), e);
            BaseResponse errorMessage = BaseResponseUtil.createErrorBaseResponse(e.getMessage());
            log.error("Error response sent: {}", errorMessage);
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "update Destination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Destination updated successfully",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request data",
                    content = @Content(schema = @Schema(implementation = Exception.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateDestination(@RequestBody UpdateDestination updateDestination) {
        try {
            log.info("Received a request to update Destination.");
            log.debug("Request data: {}", updateDestination);
            BaseResponse updatedDestination = destinationService.updateDestination(updateDestination);
            log.info("Destination updated successfully. Destination details: {}", updatedDestination);
            return new ResponseEntity<>(updatedDestination, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while creating Destination: {}", e.getMessage(), e);
            BaseResponse errorMessage = BaseResponseUtil.createErrorBaseResponse(e.getMessage());
            log.error("Error response sent: {}", errorMessage);
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint for filtering Destination's based on criteria.
     *
     * @param destinationFilterRequest The request body containing filter criteria for Destination's.
     * @return ResponseEntity containing the filtered Destination's or an error response.
     */
    @Operation(summary = "Filter Destination's")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Destination's filtered successfully",
                    content = @Content(schema = @Schema(implementation = SearchResult.class))),
            @ApiResponse(responseCode = "400", description = "Bad request data",
                    content = @Content(schema = @Schema(implementation = Exception.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping(value = "/filter", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity filterActivities(@RequestBody DestinationFilterRequest destinationFilterRequest) {
        try {
            // Log the start of the Destination filtering process.
            log.info("Received a request to filter Destination's.");

            // Log information about the incoming filter criteria for documentation purposes.
            log.debug("Filter criteria: {}", destinationFilterRequest);

            // Call the service to filter the Destination's.
            BaseResponse filteredActivities = destinationService.filterDestination(destinationFilterRequest);

            // Log information about the successful filtering for documentation purposes.
            log.info("Destination's filtered successfully. Filtered details: {}", filteredActivities);

            // Return a success response with the filtered Destination's.
            return new ResponseEntity<>(filteredActivities, HttpStatus.OK);
        } catch (Exception e) {
            // Log an error message if an exception occurs.
            log.error("Error occurred while filtering Destination's: {}", e.getMessage(), e);

            // Create an error response with the error message.
            BaseResponse errorMessage = BaseResponseUtil.createErrorBaseResponse(e.getMessage());

            // Log the error response for documentation purposes.
            log.error("Error response sent: {}", errorMessage);

            // Return an error response with the error message.
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint for retrieving information about a specific Destination based on its ID.
     *
     * @param destinationId The ID of the Destination to retrieve.
     * @return ResponseEntity containing the requested Destination or an error response.
     */
    @Operation(summary = "Get a Destination by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Destination retrieved successfully",
                    content = @Content(schema = @Schema(implementation = DestinationEntity.class))),
            @ApiResponse(responseCode = "404", description = "Destination not found",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @GetMapping(value = "/{destinationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getDestinationById(@PathVariable String destinationId) {
        try {
            // Log the start of the Destination retrieval process.
            log.info("Received a request to retrieve a Destination by ID: {}", destinationId);

            // Call the service to get the Destination by ID.
            BaseResponse retrievedDestination = destinationService.getDestinationById(destinationId);

            if (retrievedDestination != null) {
                // Log information about the successful retrieval for documentation purposes.
                log.info("Destination retrieved successfully. Retrieved details: {}", retrievedDestination);

                // Return a success response with the retrieved Destination.
                return new ResponseEntity<>(retrievedDestination, HttpStatus.OK);
            } else {
                // Log a not found message if the Destination with the given ID is not found.
                log.warn("Destination not found with ID: {}", destinationId);

                // Create a not found response.
                BaseResponse notFoundMessage = BaseResponseUtil.createErrorBaseResponse("Destination not found with ID: " + destinationId);

                // Return a not found response.
                return new ResponseEntity<>(notFoundMessage, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            // Log an error message if an exception occurs.
            log.error("Error occurred while retrieving a Destination: {}", e.getMessage(), e);

            // Create an error response with the error message.
            BaseResponse errorMessage = BaseResponseUtil.createErrorBaseResponse(e.getMessage());

            // Log the error response for documentation purposes.
            log.error("Error response sent: {}", errorMessage);

            // Return an error response with the error message.
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
