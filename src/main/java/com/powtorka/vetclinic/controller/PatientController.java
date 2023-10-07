package com.powtorka.vetclinic.controller;

import com.powtorka.vetclinic.model.patient.*;
import com.powtorka.vetclinic.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/patient")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;
    private final ModelMapper modelMapper;

    @GetMapping("/{id}")
    private ResponseEntity<PatientDto> findById(@PathVariable("id") Long id) {
        Patient patient = patientService.findById(id);
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

    @PutMapping
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
