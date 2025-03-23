package com.example.remitly.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SwiftCodeDTO {
    String address;
    String bankName;
    String countryISO2;
    String countryName;

    @JsonProperty("isHeadquarter")
    boolean isHeadquarter;
    String swiftCode;
    List<BranchDTO> branches;
}