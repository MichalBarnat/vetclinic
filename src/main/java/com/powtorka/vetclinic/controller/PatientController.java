package com.powtorka.vetclinic.controller;

import com.powtorka.vetclinic.model.patient.*;
import com.powtorka.vetclinic.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/patient")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;
    private final ModelMapper modelMapper;

    @GetMapping("/{id}")
    private ResponseEntity<PatientDto> findById(@PathVariable("id") Long id) {
        Patient patient = patientService.findById(id);
        if (patient == null) {
            return ResponseEntity.status(NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(modelMapper.map(patient, PatientDto.class));
    }

    @PostMapping
    private ResponseEntity<PatientDto> save(@RequestBody CreatePatientCommand command) {
        Patient toSave = modelMapper.map(command, Patient.class);
        Patient savedPatient = patientService.save(toSave);
        return new ResponseEntity<>(modelMapper.map(savedPatient, PatientDto.class), CREATED);
    }

    @GetMapping
    private ResponseEntity<List<PatientDto>> findAll(CreatePatientPageCommand command) {
        Pageable pageable = modelMapper.map(command, Pageable.class);
        Page<Patient> patientPage = patientService.findAll(pageable);

        if (patientPage == null || patientPage.getContent() == null) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        List<PatientDto> list = patientPage.getContent()
                .stream()
                .map(patient -> modelMapper.map(patient, PatientDto.class))
                .toList();
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deleteById(@PathVariable("id") Long id) {
        patientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deleteAll")
    private ResponseEntity<Void> deleteAll() {
        patientService.deleteAll();
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    private ResponseEntity<PatientDto> edit(@PathVariable("id") Long id, @RequestBody UdpatePatientCommand command) {
        Patient editedPatient = patientService.editPatient(id, command);
        return ResponseEntity.ok(modelMapper.map(editedPatient, PatientDto.class));
    }

    @PatchMapping("/{id}")
    private ResponseEntity<PatientDto> editPartially(@PathVariable("id") Long id, @RequestBody UdpatePatientCommand command) {
        Patient editedPatient = patientService.editPartially(id, command);
        return ResponseEntity.ok(modelMapper.map(editedPatient, PatientDto.class));
    }

    @GetMapping("/the-oldest")
    private ResponseEntity<List<Patient>> getTheOldestPatients(@RequestParam(name = "minAge", required = false, defaultValue = "14") int minAge) {
        List<Patient> theOldestPatients = patientService.findPatientsWithAgeGreaterThan(minAge);
        return ResponseEntity.ok(theOldestPatients);
    }

}
