package com.example.remitly.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SwiftCode {
    @Id
    private String swiftCode;
    private String countryIso2;
    private String bankName;
    private String address;
    private String countryName;
    private boolean isHeadquarter;
}

