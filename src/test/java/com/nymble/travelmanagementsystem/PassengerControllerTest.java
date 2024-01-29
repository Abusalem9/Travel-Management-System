package com.nymble.travelmanagementsystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nymble.travelmanagementsystem.constants.PassengerType;
import com.nymble.travelmanagementsystem.controller.PassengerController;
import com.nymble.travelmanagementsystem.entity.PassengerEntity;
import com.nymble.travelmanagementsystem.request.CreatePassengerV1;
import com.nymble.travelmanagementsystem.response.BaseResponse;
import com.nymble.travelmanagementsystem.response.TravelPackageResponse;
import com.nymble.travelmanagementsystem.service.PassengerService;
import com.nymble.travelmanagementsystem.utils.BaseResponseUtil;
import com.nymble.travelmanagementsystem.utils.StatusCode;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PassengerController.class)
public class PassengerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private PassengerService passengerService;

    @InjectMocks
    private PassengerController passengerController;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreatePassenger_Success() throws Exception {
        // Given
        CreatePassengerV1 request = createSampleRequest();

        BaseResponse response = createSampleResponse();

        when(passengerService.createPassenger(any(CreatePassengerV1.class))).thenReturn(response);

        // When
        ResultActions result = mockMvc.perform(post("/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // Then
        result.andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void testCreatePassenger_Failure() throws Exception {
        // Given
        CreatePassengerV1 request = createSampleRequest();

        when(passengerService.createPassenger(any(CreatePassengerV1.class))).thenThrow(new RuntimeException("Some error"));

        // When
        ResultActions result = mockMvc.perform(post("/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // Then
        result.andExpect(status().isInternalServerError())
                .andReturn();
    }

    private CreatePassengerV1 createSampleRequest() {
        CreatePassengerV1 sampleRequest = new CreatePassengerV1();
        sampleRequest.setPassengerName("Abusalem");
        sampleRequest.setPassengerNumber("8668992680");
        sampleRequest.setPassengerType(PassengerType.GOLD);
        sampleRequest.setPassengerBalance(BigDecimal.valueOf(100.0));
        sampleRequest.setCreatedBy("test");
        sampleRequest.setCreatedByName("test name");
        return sampleRequest;
    }

    private BaseResponse createSampleResponse() {
        PassengerEntity passengerData = new PassengerEntity();
        passengerData.setPassengerId("P2024012800002");
        passengerData.setPassengerName("Abusalem");
        passengerData.setPassengerNumber("8668992681");
        passengerData.setPassengerType("GOLD");
        passengerData.setPassengerBalance(BigDecimal.valueOf(1000));
        passengerData.setDeleted(false);
        passengerData.setActive(true);
        passengerData.setCreatedBy("test");
        passengerData.setCreatedByName("test name");
        passengerData.setUpdatedBy(null);
        passengerData.setUpdatedByName(null);
        passengerData.setCreatedAt(LocalDateTime.parse("2024-01-28T17:33:53.1231601"));
        passengerData.setUpdatedAt(LocalDateTime.parse("2024-01-28T17:33:53.1231601"));
        return BaseResponseUtil.createBaseResponse(new TravelPackageResponse(passengerData), StatusCode.OK);
    }
}
