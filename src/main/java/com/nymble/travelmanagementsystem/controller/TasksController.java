package com.nymble.travelmanagementsystem.controller;

import com.nymble.travelmanagementsystem.response.BaseResponse;
import com.nymble.travelmanagementsystem.service.TaskService;
import com.nymble.travelmanagementsystem.utils.BaseResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/task")
@Slf4j
public class TasksController {

    @Autowired
    TaskService taskService;

    @Operation(summary = "Retrieve an output of the first task. Using travelPackageId.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "First Task Output retrieved.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request data",
                    content = @Content(schema = @Schema(implementation = Exception.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @PostMapping(value = "/1/{travel-package-id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createPassenger(@PathVariable("travel-package-id") String travelPackageId) {
        try {
            BaseResponse savedPassenger = taskService.retrieveOutputOfTheFirstTask(travelPackageId);
            return new ResponseEntity<>(savedPassenger, HttpStatus.OK);
        } catch (Exception e) {
            BaseResponse errorMessage = BaseResponseUtil.createErrorBaseResponse(e.getMessage());
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Retrieve an output of the Second task. Using travelPackageId.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieve an output of the Second task. Using travelPackageId.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request data",
                    content = @Content(schema = @Schema(implementation = Exception.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @PostMapping(value = "/2/{travel-package-id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity secondTaskOutput(@PathVariable("travel-package-id") String travelPackageId) {
        try {
            BaseResponse savedPassenger = taskService.retrieveOutputOfTheSecondTask(travelPackageId);
            return new ResponseEntity<>(savedPassenger, HttpStatus.OK);
        } catch (Exception e) {
            BaseResponse errorMessage = BaseResponseUtil.createErrorBaseResponse(e.getMessage());
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Retrieve an output of the third task. Using passenger Id or passengerNo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieve an output of the third task. Using passenger Id or passengerNo.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request data",
                    content = @Content(schema = @Schema(implementation = Exception.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @PostMapping(value = "/3/{travel-package-id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity thirdTaskOutput(@PathVariable("travel-package-id") String travelPackageId) {
        try {
            BaseResponse savedPassenger = taskService.retrieveOutputOfTheThirdTask(travelPackageId);
            return new ResponseEntity<>(savedPassenger, HttpStatus.OK);
        } catch (Exception e) {
            BaseResponse errorMessage = BaseResponseUtil.createErrorBaseResponse(e.getMessage());
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Retrieve an output of the fourth task - Available activities. Using travelPackageId.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "First Task Output retrieved.",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad request data",
                    content = @Content(schema = @Schema(implementation = Exception.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = Exception.class)))
    })
    @PostMapping(value = "/4/{travel-package-id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity fourthTaskOutput(@PathVariable("travel-package-id") String travelPackageId) {
        try {
            BaseResponse savedPassenger = taskService.retrieveOutputOfTheFourthTask(travelPackageId);
            return new ResponseEntity<>(savedPassenger, HttpStatus.OK);
        } catch (Exception e) {
            BaseResponse errorMessage = BaseResponseUtil.createErrorBaseResponse(e.getMessage());
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
