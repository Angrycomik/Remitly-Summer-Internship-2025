package com.example.remitly.repository;

import com.example.remitly.model.SwiftCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SwiftCodeRepository extends JpaRepository<SwiftCode, String> {
    List<SwiftCode> findByCountryIso2(String countryIso2);
    List<SwiftCode> findBySwiftCodeStartingWith(String swiftPrefix);


}
