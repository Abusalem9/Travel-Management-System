package com.nymble.travelmanagementsystem.controller;

import com.nymble.travelmanagementsystem.entity.PassengerEntity;
import com.nymble.travelmanagementsystem.request.CreatePassengerV1;
import com.nymble.travelmanagementsystem.request.PassengerFilterRequest;
import com.nymble.travelmanagementsystem.request.UpdatePassenger;
import com.nymble.travelmanagementsystem.response.BaseResponse;
import com.nymble.travelmanagementsystem.service.PassengerService;
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
@RequestMapping("/passenger")
@Slf4j
public class PassengerController {

    @Autowired
    PassengerService passengerService;

    @Operation(summary = "create Passenger")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Passenger created successfully",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request data",
                    content = @Content(schema = @Schema(implementation = Exception.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createPassenger(@RequestBody CreatePassengerV1 createPassenger) {
        try {
            log.info("Received a request to create Passenger.");
            log.debug("Request data: {}", createPassenger);
            BaseResponse savedPassenger = passengerService.createPassenger(createPassenger);
            log.info("Passenger created successfully. Passenger details: {}", savedPassenger);
            return new ResponseEntity<>(savedPassenger, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while creating Passenger: {}", e.getMessage(), e);
            BaseResponse errorMessage = BaseResponseUtil.createErrorBaseResponse(e.getMessage());
            log.error("Error response sent: {}", errorMessage);
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "update Passenger")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Passenger updated successfully",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request data",
                    content = @Content(schema = @Schema(implementation = Exception.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updatePassenger(@RequestBody UpdatePassenger updatePassenger) {
        try {
            log.info("Received a request to update Passenger.");
            log.debug("Request data: {}", updatePassenger);
            BaseResponse updatedPassenger = passengerService.updatePassenger(updatePassenger);
            log.info("Passenger updated successfully. Passenger details: {}", updatedPassenger);
            return new ResponseEntity<>(updatedPassenger, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while creating Passenger: {}", e.getMessage(), e);
            BaseResponse errorMessage = BaseResponseUtil.createErrorBaseResponse(e.getMessage());
            log.error("Error response sent: {}", errorMessage);
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Endpoint for filtering Passenger's based on criteria.
     *
     * @param passengerFilterRequest The request body containing filter criteria for Passenger's.
     * @return ResponseEntity containing the filtered Passenger's or an error response.
     */
    @Operation(summary = "Filter Passenger's")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Passenger's filtered successfully",
                    content = @Content(schema = @Schema(implementation = SearchResult.class))),
            @ApiResponse(responseCode = "400", description = "Bad request data",
                    content = @Content(schema = @Schema(implementation = Exception.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping(value = "/filter", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity filterActivities(@RequestBody PassengerFilterRequest passengerFilterRequest) {
        try {
            // Log the start of the Passenger filtering process.
            log.info("Received a request to filter Passenger's.");

            // Log information about the incoming filter criteria for documentation purposes.
            log.debug("Filter criteria: {}", passengerFilterRequest);

            // Call the service to filter the Passenger's.
            BaseResponse filteredActivities = passengerService.filterPassenger(passengerFilterRequest);

            // Log information about the successful filtering for documentation purposes.
            log.info("Passenger's filtered successfully. Filtered details: {}", filteredActivities);

            // Return a success response with the filtered Passenger's.
            return new ResponseEntity<>(filteredActivities, HttpStatus.OK);
        } catch (Exception e) {
            // Log an error message if an exception occurs.
            log.error("Error occurred while filtering Passenger's: {}", e.getMessage(), e);

            // Create an error response with the error message.
            BaseResponse errorMessage = BaseResponseUtil.createErrorBaseResponse(e.getMessage());

            // Log the error response for documentation purposes.
            log.error("Error response sent: {}", errorMessage);

            // Return an error response with the error message.
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint for retrieving information about a specific Passenger based on its ID.
     *
     * @param passengerId The ID of the Passenger to retrieve.
     * @return ResponseEntity containing the requested Passenger or an error response.
     */
    @Operation(summary = "Get a Passenger by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Passenger retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PassengerEntity.class))),
            @ApiResponse(responseCode = "404", description = "Passenger not found",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @GetMapping(value = "/{passengerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getPassengerById(@PathVariable String passengerId) {
        try {
            // Log the start of the Passenger retrieval process.
            log.info("Received a request to retrieve a Passenger by ID: {}", passengerId);

            // Call the service to get the Passenger by ID.
            BaseResponse retrievedPassenger = passengerService.getPassengerById(passengerId);

            if (retrievedPassenger != null) {
                // Log information about the successful retrieval for documentation purposes.
                log.info("Passenger retrieved successfully. Retrieved details: {}", retrievedPassenger);

                // Return a success response with the retrieved Passenger.
                return new ResponseEntity<>(retrievedPassenger, HttpStatus.OK);
            } else {
                // Log a not found message if the Passenger with the given ID is not found.
                log.warn("Passenger not found with ID: {}", passengerId);

                // Create a not found response.
                BaseResponse notFoundMessage = BaseResponseUtil.createErrorBaseResponse("Passenger not found with ID: " + passengerId);

                // Return a not found response.
                return new ResponseEntity<>(notFoundMessage, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            // Log an error message if an exception occurs.
            log.error("Error occurred while retrieving a Passenger: {}", e.getMessage(), e);

            // Create an error response with the error message.
            BaseResponse errorMessage = BaseResponseUtil.createErrorBaseResponse(e.getMessage());

            // Log the error response for documentation purposes.
            log.error("Error response sent: {}", errorMessage);

            // Return an error response with the error message.
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
