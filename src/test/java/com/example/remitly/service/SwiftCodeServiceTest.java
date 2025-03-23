package com.example.remitly.service;

import com.example.remitly.model.SwiftCode;
import com.example.remitly.repository.SwiftCodeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SwiftCodeServiceTest {

    @Mock
    private SwiftCodeRepository repository;

    @InjectMocks
    private SwiftCodeService service;


    @Test
    void shouldReturnSwiftCodeDetails_whenSwiftCodeExists(){
        SwiftCode swiftCode = new SwiftCode("BKSACLRMXXX",  "CL","SCOTIABANK CHILE",
                "AVENIDA COSTANERA SUR 2710, FLOOR 10 EDIFICIO PARQUE TITANIUM SANTIAGO, PROVINCIA DE SANTIAGO","Chile", true);
        when(repository.findById("BKSACLRMXXX")).thenReturn(Optional.of(swiftCode));

        Optional<SwiftCode> result = service.getSwiftCode("BKSACLRMXXX");

        assertTrue(result.isPresent());
        assertEquals("BKSACLRMXXX", result.get().getSwiftCode());
        assertTrue(result.get().isHeadquarter());
    }

    @Test
    void shouldReturnBranchesForHeadquarter() {
        SwiftCode branch1 = new SwiftCode("BKSACLRM061",  "CL","SCOTIABANK CHILE","","Chile", false);
        SwiftCode branch2 = new SwiftCode("BKSACLRM068",  "CL","SCOTIABANK CHILE","","Chile", false);
        when(repository.findBySwiftCodeStartingWith("BKSACLRM")).thenReturn(List.of(branch1, branch2));

        List<SwiftCode> branches = service.getBranches("BKSACLRM");

        assertEquals(2, branches.size());
        assertFalse(branches.get(0).isHeadquarter());
    }
    @Test
    void shouldThrowException_whenSwiftCodeExists() {
        SwiftCode existingCode = new SwiftCode("BKSACLRM061",  "CL","SCOTIABANK CHILE","","Chile", false);

        when(repository.findById("BKSACLRM061")).thenReturn(Optional.of(existingCode));

        assertThrows(DuplicateKeyException.class, () -> service.saveSwiftCode(existingCode));
    }

    @Test
    void shouldDeleteSwiftCode() {
        when(repository.existsById("BKSACLRM061")).thenReturn(true);

        service.deleteSwiftCode("BKSACLRM061");

        verify(repository).deleteById("BKSACLRM061");
    }

    @Test
    void shouldThrowException_whenDeletingNonExistingSwiftCode() {
        when(repository.existsById("BKSACLRM061")).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> service.deleteSwiftCode("BKSACLRM061"));
    }

}
