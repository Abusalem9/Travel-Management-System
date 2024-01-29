package com.nymble.travelmanagementsystem.controller;

import com.nymble.travelmanagementsystem.entity.BookingEntity;
import com.nymble.travelmanagementsystem.request.BookingFilterRequest;
import com.nymble.travelmanagementsystem.request.CreateBooking;
import com.nymble.travelmanagementsystem.request.CreateBookingV1;
import com.nymble.travelmanagementsystem.request.UpdateBooking;
import com.nymble.travelmanagementsystem.response.BaseResponse;
import com.nymble.travelmanagementsystem.service.BookingService;
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
@RequestMapping("/booking")
@Slf4j
public class BookingController {
    @Autowired
    BookingService bookingService;

    @Operation(summary = "create Booking")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking created successfully",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request data",
                    content = @Content(schema = @Schema(implementation = Exception.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createBooking(@RequestBody CreateBooking createBooking) {
        try {
            log.info("Received a request to create Booking.");
            log.debug("Request data: {}", createBooking);
            BaseResponse savedBooking = bookingService.createBooking(createBooking);
            log.info("Booking created successfully. Booking details: {}", savedBooking);
            return new ResponseEntity<>(savedBooking, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while creating Booking: {}", e.getMessage(), e);
            BaseResponse errorMessage = BaseResponseUtil.createErrorBaseResponse(e.getMessage());
            log.error("Error response sent: {}", errorMessage);
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Create single or multiple bookings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking created successfully",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request data",
                    content = @Content(schema = @Schema(implementation = Exception.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @PostMapping(value = "/multiple/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createSingleOrMultipleBooking(@RequestBody CreateBookingV1 createBooking) {
        try {
            log.info("Received a request to create Booking.");
            log.debug("Request data: {}", createBooking);
            BaseResponse savedBooking = bookingService.createSingleOrMultipleBooking(createBooking);
            log.info("Booking created successfully. Booking details: {}", savedBooking);
            return new ResponseEntity<>(savedBooking, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while creating Booking: {}", e.getMessage(), e);
            BaseResponse errorMessage = BaseResponseUtil.createErrorBaseResponse(e.getMessage());
            log.error("Error response sent: {}", errorMessage);
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "update Booking")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking updated successfully",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request data",
                    content = @Content(schema = @Schema(implementation = Exception.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateBooking(@RequestBody UpdateBooking updateBooking) {
        try {
            log.info("Received a request to update Booking.");
            log.debug("Request data: {}", updateBooking);
            BaseResponse savedBooking = bookingService.updateBooking(updateBooking);
            log.info("Booking updated successfully. Booking details: {}", savedBooking);
            return new ResponseEntity<>(savedBooking, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while creating Booking: {}", e.getMessage(), e);
            BaseResponse errorMessage = BaseResponseUtil.createErrorBaseResponse(e.getMessage());
            log.error("Error response sent: {}", errorMessage);
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    /**
     * Endpoint for filtering Booking's based on criteria.
     *
     * @param bookingFilterRequest The request body containing filter criteria for Booking's.
     * @return ResponseEntity containing the filtered Booking's or an error response.
     */
    @Operation(summary = "Filter Booking's")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking's filtered successfully",
                    content = @Content(schema = @Schema(implementation = SearchResult.class))),
            @ApiResponse(responseCode = "400", description = "Bad request data",
                    content = @Content(schema = @Schema(implementation = Exception.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping(value = "/filter", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity filterActivities(@RequestBody BookingFilterRequest bookingFilterRequest) {
        try {
            // Log the start of the Booking filtering process.
            log.info("Received a request to filter Booking's.");

            // Log information about the incoming filter criteria for documentation purposes.
            log.debug("Filter criteria: {}", bookingFilterRequest);

            // Call the service to filter the Booking's.
            BaseResponse filteredActivities = bookingService.filterBooking(bookingFilterRequest);

            // Log information about the successful filtering for documentation purposes.
            log.info("Booking's filtered successfully. Filtered details: {}", filteredActivities);

            // Return a success response with the filtered Booking's.
            return new ResponseEntity<>(filteredActivities, HttpStatus.OK);
        } catch (Exception e) {
            // Log an error message if an exception occurs.
            log.error("Error occurred while filtering Booking's: {}", e.getMessage(), e);

            // Create an error response with the error message.
            BaseResponse errorMessage = BaseResponseUtil.createErrorBaseResponse(e.getMessage());

            // Log the error response for documentation purposes.
            log.error("Error response sent: {}", errorMessage);

            // Return an error response with the error message.
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint for retrieving information about a specific Booking based on its ID.
     *
     * @param bookingId The ID of the Booking to retrieve.
     * @return ResponseEntity containing the requested Booking or an error response.
     */
    @Operation(summary = "Get a Booking by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking retrieved successfully",
                    content = @Content(schema = @Schema(implementation = BookingEntity.class))),
            @ApiResponse(responseCode = "404", description = "Booking not found",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorMessage.class)))
    })
    @GetMapping(value = "/{bookingId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getBookingById(@PathVariable String bookingId) {
        try {
            // Log the start of the Booking retrieval process.
            log.info("Received a request to retrieve a Booking by ID: {}", bookingId);

            // Call the service to get the Booking by ID.
            BaseResponse retrievedBooking = bookingService.getBookingById(bookingId);

            if (retrievedBooking != null) {
                // Log information about the successful retrieval for documentation purposes.
                log.info("Booking retrieved successfully. Retrieved details: {}", retrievedBooking);

                // Return a success response with the retrieved Booking.
                return new ResponseEntity<>(retrievedBooking, HttpStatus.OK);
            } else {
                // Log a not found message if the Booking with the given ID is not found.
                log.warn("Booking not found with ID: {}", bookingId);

                // Create a not found response.
                BaseResponse notFoundMessage = BaseResponseUtil.createErrorBaseResponse("Booking not found with ID: " + bookingId);

                // Return a not found response.
                return new ResponseEntity<>(notFoundMessage, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            // Log an error message if an exception occurs.
            log.error("Error occurred while retrieving a Booking: {}", e.getMessage(), e);

            // Create an error response with the error message.
            BaseResponse errorMessage = BaseResponseUtil.createErrorBaseResponse(e.getMessage());

            // Log the error response for documentation purposes.
            log.error("Error response sent: {}", errorMessage);

            // Return an error response with the error message.
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
