package com.example.remitly.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SwiftByISO2DTO {
    private String countryISO2;
    private String countryName;
    private List<SwiftCodeDTO> swiftCodes;

}
