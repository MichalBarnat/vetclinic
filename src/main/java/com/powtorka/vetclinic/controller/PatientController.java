package com.powtorka.vetclinic.controller;

import com.powtorka.vetclinic.exceptions.PatientNotFoundException;
import com.powtorka.vetclinic.model.patient.*;
import com.powtorka.vetclinic.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patient")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;
    private final ModelMapper modelMapper;

    @GetMapping("/{id}")
    private PatientDto findById(@PathVariable("id") Long id) {
        Patient patient = patientService.findById(id);
        return modelMapper.map(patient, PatientDto.class);

    }

    @PostMapping
    PatientDto save(@RequestBody CreatePatientCommand command) {
        Patient toSave = modelMapper.map(command, Patient.class);
        Patient savedPatient = patientService.save(toSave);
        return modelMapper.map(savedPatient, PatientDto.class);
    }

    @GetMapping
    private List<PatientDto> findAll(CreatePatientPageCommand command) {
        Pageable pageable = modelMapper.map(command, Pageable.class);
        Page<Patient> patientPage = patientService.findAll(pageable);
        return patientPage.getContent()
                .stream()
                .map(patient -> modelMapper.map(patient, PatientDto.class))
                .toList();
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
        patientService.deleteById(id);
        return ResponseEntity.ok("Patient with ID: " + id + " has been deleted");
    }

    @PutMapping
    private PatientDto edit(@PathVariable("id") Long id, @RequestBody UdpatePatientCommand command) {
        Patient editedPatient = patientService.editPatient(id, command);
        return modelMapper.map(editedPatient, PatientDto.class);
    }

    @PatchMapping("/{id}")
    private PatientDto editPartially(@PathVariable("id") Long id, @RequestBody UdpatePatientCommand command) {
        Patient editedPatient = patientService.editPartially(id, command);
        return modelMapper.map(editedPatient, PatientDto.class);
    }

    @GetMapping("/the-oldest")
    private ResponseEntity<List<Patient>> getTheOldestPatients(@RequestParam(name = "minAge", required = false, defaultValue = "14") int minAge) {
        List<Patient> theOldestPatients = patientService.findPatientsWithAgeGreaterThan(minAge);
        return ResponseEntity.ok(theOldestPatients);
    }

}
