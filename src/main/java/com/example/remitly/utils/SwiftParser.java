package com.example.remitly.utils;

import com.example.remitly.model.SwiftCode;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;

public class SwiftParser {
    public static ArrayList<SwiftCode> parseCSV(String file){
        ArrayList<SwiftCode> swiftCodes = new ArrayList<>();

        try(Reader in = new FileReader(file)){

            CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader();

            Iterable<CSVRecord> records = csvFormat.parse(in);

            //Town, time zone and code type columns are redundant
            for (CSVRecord record : records) {
                String swiftCode = record.get("SWIFT CODE");
                String bankName = record.get("NAME");
                String address = record.get("ADDRESS");
                String countryIso2 = record.get("COUNTRY ISO2 CODE").toUpperCase();
                String countryName = record.get("COUNTRY NAME").toUpperCase();
                boolean isHeadquarter = swiftCode.endsWith("XXX");
                System.out.println("Parsed: " + swiftCode);
                swiftCodes.add(new SwiftCode(swiftCode, countryIso2, bankName, address, countryName, isHeadquarter));
            }
        }catch (Exception e){
            System.out.println("Failed to load data: " + e.getMessage());
            e.printStackTrace();

        }

    return swiftCodes;
    }

}
