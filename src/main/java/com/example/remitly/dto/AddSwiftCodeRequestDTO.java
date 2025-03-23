package com.example.remitly.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddSwiftCodeRequestDTO {
    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Bank name is required")
    private String bankName;

    @NotBlank(message = "Country ISO2 code is required")
    @Size(min = 2, max = 2, message = "Country ISO2 code must be 2 characters")
    private String countryISO2;

    @NotBlank(message = "Country name is required")
    private String countryName;

    @JsonProperty(value = "isHeadquarter", required = true)
    @NotNull( message = "Headquarter flag is required")
    private boolean isHeadquarter;

    @NotBlank(message = "SWIFT code is required")
    @Size(min = 8, max = 11, message = "SWIFT code must be between 8 and 11 characters")
    private String swiftCode;

}