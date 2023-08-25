package com.powtorka.vetclinic.controller;

import com.powtorka.vetclinic.model.patient.Patient;
import com.powtorka.vetclinic.model.patient.CreatePatientCommand;
import com.powtorka.vetclinic.model.patient.PatientDto;
import com.powtorka.vetclinic.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patient")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    @GetMapping("/{id}")
    private PatientDto findById(@PathVariable("id") Long id) {
        Patient patient = patientService.findById(id);
        return PatientDto.fromPatient(patient);
    }

    @PostMapping
    public PatientDto save(@RequestBody CreatePatientCommand command) {
        Patient toSave = CreatePatientCommand.toPatient(command);
        Patient savedPatient = patientService.save(toSave);
        return PatientDto.fromPatient(savedPatient);
    }

    @GetMapping
    private List<PatientDto> findAll() {
        return patientService.findAll()
                .stream()
                .map(PatientDto::fromPatient)
                .toList();
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
        patientService.deleteById(id);
        return ResponseEntity.ok("Patient witd ID: " + id + " has been deleted");
    }


}
