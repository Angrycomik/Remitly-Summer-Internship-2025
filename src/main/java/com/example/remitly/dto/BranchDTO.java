package com.example.remitly.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BranchDTO {
    private String address;
    private String bankName;
    private String countryISO2;

    @JsonProperty("isHeadquarter")
    private boolean isHeadquarter;
    private String swiftCode;
}
