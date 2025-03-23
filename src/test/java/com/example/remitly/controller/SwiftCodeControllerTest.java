package com.example.remitly.controller;

import com.example.remitly.dto.AddSwiftCodeRequestDTO;
import com.example.remitly.model.SwiftCode;
import com.example.remitly.service.SwiftCodeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class SwiftCodeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SwiftCodeService service;

    @InjectMocks
    private SwiftCodeController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }



    @Test
    void shouldReturnSwiftCodeDetails_whenSwiftCodeExists() throws Exception {
        SwiftCode swiftCode = new SwiftCode("BKSACLRM061",  "CL","SCOTIABANK CHILE","Address","Chile", false);
        when(service.getSwiftCode("BKSACLRMXXX")).thenReturn(Optional.of(swiftCode));

        mockMvc.perform(get("/v1/swift-codes/BKSACLRMXXX"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.swiftCode").value("BKSACLRM061"))
                .andExpect(jsonPath("$.bankName").value("SCOTIABANK CHILE"))
                .andExpect(jsonPath("$.isHeadquarter").value(false));
    }

    @Test
    void shouldReturnNotFound_whenSwiftCodeNotExists() throws Exception {
        when(service.getSwiftCode("MISSING")).thenReturn(Optional.empty());

        mockMvc.perform(get("/v1/swift-codes/MISSING"))
                .andExpect(status().isNotFound());
    }
    @Test
    void shouldReturnSwiftCodes_whenCountryISO2Exists() throws Exception {
        SwiftCode swift1 = new SwiftCode("BKSACLRM061",  "CL","SCOTIABANK CHILE","Address","Chile", false);
        SwiftCode swift2 = new SwiftCode("BKSACLRM068",  "CL","SCOTIABANK CHILE","Address","Chile", false);

        when(service.getSwiftCodesByISO2("CL")).thenReturn(List.of(swift1, swift2));

        mockMvc.perform(get("/v1/swift-codes/country/cl"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countryISO2").value("CL"))
                .andExpect(jsonPath("$.countryName").value("Chile"))
                .andExpect(jsonPath("$.swiftCodes[0].swiftCode").value("BKSACLRM061"))
                .andExpect(jsonPath("$.swiftCodes[1].swiftCode").value("BKSACLRM068"));
    }
    @Test
    void shouldAddSwiftCodeSuccessfully() throws Exception {
        AddSwiftCodeRequestDTO request = new AddSwiftCodeRequestDTO( "Address","SCOTIABANK CHILE","CL","Chile", false, "BKSACLRM061");

        mockMvc.perform(post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("SWIFT code added successfully"));
    }

    @Test
    void shouldReturnConflict_whenSwiftCodeExists() throws Exception {
        AddSwiftCodeRequestDTO request = new AddSwiftCodeRequestDTO("Address" , "SCOTIABANK CHILE", "CL", "Chile", false, "BKSACLRM061");

        doThrow(new DuplicateKeyException("SWIFT code already exists: BKSACLRM061"))
                .when(service).saveSwiftCode(any(SwiftCode.class));

        mockMvc.perform(post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("SWIFT code already exists: BKSACLRM061"));
    }
    @Test
    void shouldReturnNoContent_whenDeleteSwiftCodeExists() throws Exception {

        doNothing().when(service).deleteSwiftCode("BKSACLRMXXX");

        mockMvc.perform(delete("/v1/swift-codes/BKSACLRMXXX")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("SWIFT code deleted successfully"));;
    }
    @Test
    void shouldReturnNotFound_whenDeleteSwiftCodeNotExists() throws Exception {
        doThrow(new EntityNotFoundException("SWIFT code not found: BKSACLRMXXX"))
                .when(service).deleteSwiftCode("BKSACLRMXXX");

        mockMvc.perform(delete("/v1/swift-codes/BKSACLRMXXX")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
