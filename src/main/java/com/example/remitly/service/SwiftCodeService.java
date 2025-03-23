package com.example.remitly.service;

import com.example.remitly.model.SwiftCode;
import com.example.remitly.repository.SwiftCodeRepository;
import com.example.remitly.utils.SwiftParser;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SwiftCodeService {
    private final SwiftCodeRepository repository;

    @Autowired
    public SwiftCodeService(SwiftCodeRepository repository) {
        this.repository = repository;
    }

    public boolean loadDataFromCSV(String filePath) {
        ArrayList<SwiftCode> swiftCodes = SwiftParser.parseCSV(filePath);
        repository.saveAll(swiftCodes);
        return !swiftCodes.isEmpty();
    }

    public List<SwiftCode> getBranches(String headquartersSwift) {
        String swiftPrefix = headquartersSwift.substring(0, 8);

        return repository.findBySwiftCodeStartingWith(swiftPrefix)
                .stream()
                .filter(branch -> !branch.isHeadquarter())
                .collect(Collectors.toList());
    }

    public Optional<SwiftCode> getSwiftCode(String swiftCode) {
        return repository.findById(swiftCode);
    }

    public List<SwiftCode> getSwiftCodesByISO2(String countryIso2) {
        return repository.findByCountryIso2(countryIso2);
    }

    public void saveSwiftCode(SwiftCode swiftCode) {
        if(repository.findById(swiftCode.getSwiftCode()).isPresent()) {
            throw new DuplicateKeyException("SWIFT code already exists: " + swiftCode.getSwiftCode());
        }
        repository.save(swiftCode);
    }

    public void deleteSwiftCode(String swiftCode) {
        if (!repository.existsById(swiftCode)) {
            throw new EntityNotFoundException("SWIFT code not found: " + swiftCode);
        }
        repository.deleteById(swiftCode);
    }

    //Parse .csv file after the application starts, if database is empty
    @PostConstruct
    public void initDatabase() {
        String filePath = "swift_codes.csv";
        if (repository.count() == 0) {
            if(loadDataFromCSV(filePath)){
                System.out.println("Data loaded successfully");
            }else {
                System.out.println("Failed to load data");
            }

        }
    }
}
