package com.nymble.travelmanagementsystem.controller;

import com.nymble.travelmanagementsystem.entity.TravelPackageEntity;
import com.nymble.travelmanagementsystem.request.CreateTravelPackage;
import com.nymble.travelmanagementsystem.request.TravelPackageFilterRequest;
import com.nymble.travelmanagementsystem.request.UpdateTravelPackage;
import com.nymble.travelmanagementsystem.response.BaseResponse;
import com.nymble.travelmanagementsystem.service.TravelService;
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
@RequestMapping("/package")
@Slf4j
public class TravelPackageController {

    @Autowired
    TravelService travelPackageService;

    @Operation(summary = "create TravelPackage")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "TravelPackage created successfully",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request data",
                    content = @Content(schema = @Schema(implementation = Exception.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createTravelPackage(@RequestBody CreateTravelPackage createTravelPackage) {
        try {
            log.info("Received a request to create TravelPackage.");
            log.debug("Request data: {}", createTravelPackage);
            BaseResponse savedTravelPackage = travelPackageService.createTravelPackage(createTravelPackage);
            log.info("TravelPackage created successfully. TravelPackage details: {}", savedTravelPackage);
            return new ResponseEntity<>(savedTravelPackage, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while creating TravelPackage: {}", e.getMessage(), e);
            BaseResponse errorMessage = BaseResponseUtil.createErrorBaseResponse(e.getMessage());
            log.error("Error response sent: {}", errorMessage);
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "update TravelPackage")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "TravelPackage updated successfully",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request data",
                    content = @Content(schema = @Schema(implementation = Exception.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateTravelPackage(@RequestBody UpdateTravelPackage updateTravelPackage) {
        try {
            log.info("Received a request to update TravelPackage.");
            log.debug("Request data: {}", updateTravelPackage);
            BaseResponse updatedTravelPackage = null;//updateTravelPackage(updateTravelPackage);
            log.info("TravelPackage updated successfully. TravelPackage details: {}", updatedTravelPackage);
            return new ResponseEntity<>(updatedTravelPackage, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while creating TravelPackage: {}", e.getMessage(), e);
            BaseResponse errorMessage = BaseResponseUtil.createErrorBaseResponse(e.getMessage());
            log.error("Error response sent: {}", errorMessage);
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint for filtering TravelPackages based on criteria.
     *
     * @param travelPackageFilterRequest The request body containing filter criteria for TravelPackages.
     * @return ResponseEntity containing the filtered TravelPackages or an error response.
     */
    @Operation(summary = "Filter TravelPackages")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "TravelPackages filtered successfully",
                    content = @Content(schema = @Schema(implementation = SearchResult.class))),
            @ApiResponse(responseCode = "400", description = "Bad request data",
                    content = @Content(schema = @Schema(implementation = Exception.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping(value = "/filter", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity filterTravelPackages(@RequestBody TravelPackageFilterRequest travelPackageFilterRequest) {
        try {
            // Log the start of the TravelPackage filtering process.
            log.info("Received a request to filter TravelPackages.");

            // Log information about the incoming filter criteria for documentation purposes.
            log.debug("Filter criteria: {}", travelPackageFilterRequest);

            // Call the service to filter the TravelPackages.
            BaseResponse filteredTravelPackages = travelPackageService.filterTravelPackage(travelPackageFilterRequest);

            // Log information about the successful filtering for documentation purposes.
            log.info("TravelPackages filtered successfully. Filtered details: {}", filteredTravelPackages);

            // Return a success response with the filtered TravelPackages.
            return new ResponseEntity<>(filteredTravelPackages, HttpStatus.OK);
        } catch (Exception e) {
            // Log an error message if an exception occurs.
            log.error("Error occurred while filtering TravelPackages: {}", e.getMessage(), e);

            // Create an error response with the error message.
            BaseResponse errorMessage = BaseResponseUtil.createErrorBaseResponse(e.getMessage());

            // Log the error response for documentation purposes.
            log.error("Error response sent: {}", errorMessage);

            // Return an error response with the error message.
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Endpoint for retrieving information about a specific TravelPackage based on its ID.
     *
     * @param travelPackageId The ID of the TravelPackage to retrieve.
     * @return ResponseEntity containing the requested TravelPackage or an error response.
     */
    @Operation(summary = "Get a TravelPackage by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "TravelPackage retrieved successfully",
                    content = @Content(schema = @Schema(implementation = TravelPackageEntity.class))),
            @ApiResponse(responseCode = "404", description = "TravelPackage not found",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @GetMapping(value = "/{travelPackageId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getTravelPackageById(@PathVariable String travelPackageId) {
        try {
            // Log the start of the TravelPackage retrieval process.
            log.info("Received a request to retrieve a TravelPackage by ID: {}", travelPackageId);

            // Call the service to get the TravelPackage by ID.
            BaseResponse retrievedTravelPackage = travelPackageService.getTravelPackageById(travelPackageId);

            if (retrievedTravelPackage != null) {
                // Log information about the successful retrieval for documentation purposes.
                log.info("TravelPackage retrieved successfully. Retrieved details: {}", retrievedTravelPackage);

                // Return a success response with the retrieved TravelPackage.
                return new ResponseEntity<>(retrievedTravelPackage, HttpStatus.OK);
            } else {
                // Log a not found message if the TravelPackage with the given ID is not found.
                log.warn("TravelPackage not found with ID: {}", travelPackageId);

                // Create a not found response.
                BaseResponse notFoundMessage = BaseResponseUtil.createErrorBaseResponse("TravelPackage not found with ID: " + travelPackageId);

                // Return a not found response.
                return new ResponseEntity<>(notFoundMessage, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            // Log an error message if an exception occurs.
            log.error("Error occurred while retrieving a TravelPackage: {}", e.getMessage(), e);

            // Create an error response with the error message.
            BaseResponse errorMessage = BaseResponseUtil.createErrorBaseResponse(e.getMessage());

            // Log the error response for documentation purposes.
            log.error("Error response sent: {}", errorMessage);

            // Return an error response with the error message.
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
