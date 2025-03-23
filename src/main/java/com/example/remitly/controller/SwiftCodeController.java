package com.example.remitly.controller;

import com.example.remitly.dto.*;
import com.example.remitly.model.SwiftCode;
import com.example.remitly.service.SwiftCodeService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/swift-codes")
@Validated
public class SwiftCodeController {
    private final SwiftCodeService service;

    public SwiftCodeController(SwiftCodeService service) {
        this.service = service;
    }
    //Endpoint 1: Retrieve details of a single SWIFT code whether for a headquarters or branches.
    @GetMapping("/{swiftCode}")
    public ResponseEntity<SwiftCodeDTO> getSwiftCodeDetails(@PathVariable String swiftCode) {
        swiftCode = swiftCode.toUpperCase();
        Optional<SwiftCode> swiftCodeOptional = service.getSwiftCode(swiftCode);

        if (swiftCodeOptional.isEmpty()) {
            throw new EntityNotFoundException("Swift code " + swiftCode + " not found");
        }

        SwiftCode swift = swiftCodeOptional.get();

        List<BranchDTO> branches = swift.isHeadquarter()
                ? service.getBranches(swift.getSwiftCode()).stream()
                .map(branch -> new BranchDTO(branch.getAddress(),
                        branch.getBankName(), branch.getCountryIso2(), branch.isHeadquarter(), branch.getSwiftCode()))
                .toList()
                : List.of();

        SwiftCodeDTO response = new SwiftCodeDTO(
                swift.getAddress(), swift.getBankName(),
                swift.getCountryIso2(), swift.getCountryName(), swift.isHeadquarter(), swift.getSwiftCode(), branches );

        return ResponseEntity.ok(response);
    }

    //Endpoint 2: Return all SWIFT codes with details for a specific country (both headquarters and branches).
    @GetMapping("/country/{countryISO2}")
    public ResponseEntity<SwiftByISO2DTO> getSwiftCodesByCountry(@PathVariable String countryISO2) {
        countryISO2 = countryISO2.toUpperCase();
        List<SwiftCode> swiftCodes = service.getSwiftCodesByISO2(countryISO2);

        if (swiftCodes.isEmpty()) {
            throw new EntityNotFoundException("No swift code for country " + countryISO2 + " found");
        }

        String countryName = swiftCodes.get(0).getCountryName();

        List<SwiftCodeDTO> swiftCodeDTOs = swiftCodes.stream()
                .map(swift -> new SwiftCodeDTO(
                        swift.getAddress(),
                        swift.getBankName(),
                        swift.getCountryIso2(),
                        null,
                        swift.isHeadquarter(),
                        swift.getSwiftCode(),
                        null
                ))
                .toList();

        return ResponseEntity.ok(new SwiftByISO2DTO(countryISO2, countryName, swiftCodeDTOs));
    }

    //Endpoint 3: Adds new SWIFT code entries to the database for a specific country.
    @PostMapping
    public ResponseEntity<ResponseDTO> addSwiftCode(@RequestBody @Valid AddSwiftCodeRequestDTO request) {
        SwiftCode swiftCode = new SwiftCode();
        swiftCode.setAddress(request.getAddress());
        swiftCode.setBankName(request.getBankName());
        swiftCode.setCountryIso2(request.getCountryISO2().toUpperCase());
        swiftCode.setCountryName(request.getCountryName());
        swiftCode.setHeadquarter(request.isHeadquarter());
        swiftCode.setSwiftCode(request.getSwiftCode().toUpperCase());

        service.saveSwiftCode(swiftCode);

        return ResponseEntity.ok(new ResponseDTO("SWIFT code added successfully"));
    }

    //Endpoint 4: Deletes swift-code data if swiftCode matches the one in the database.
    @DeleteMapping("/{swiftCode}")
    public ResponseEntity<ResponseDTO> deleteSwiftCode(@PathVariable String swiftCode) {
        service.deleteSwiftCode(swiftCode.toUpperCase());
        return ResponseEntity.ok(new ResponseDTO("SWIFT code deleted successfully"));
    }
}