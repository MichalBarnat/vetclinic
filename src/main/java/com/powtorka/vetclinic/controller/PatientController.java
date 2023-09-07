package com.powtorka.vetclinic.controller;

import com.powtorka.vetclinic.exceptions.PatientWithThisIdDoNotExistException;
import com.powtorka.vetclinic.model.patient.UdpatePatientCommand;
import com.powtorka.vetclinic.model.patient.Patient;
import com.powtorka.vetclinic.model.patient.CreatePatientCommand;
import com.powtorka.vetclinic.model.patient.PatientDto;
import com.powtorka.vetclinic.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/patient")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

//    @GetMapping("/{id}")
//    private PatientDto findById(@PathVariable("id") Long id) {
//        Patient patient = patientService.findById(id);
//        return PatientDto.fromPatient(patient);
//
//    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDto> findById(@PathVariable("id") Long id) {
        try {
            Patient patient = patientService.findById(id);
            return ResponseEntity.ok(PatientDto.fromPatient(patient));
        } catch (PatientWithThisIdDoNotExistException e) {
            return ResponseEntity.notFound().build();
        }
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
        return ResponseEntity.ok("Patient with ID: " + id + " has been deleted");
    }

    @PutMapping("/{id}")
    private PatientDto edit(@PathVariable("id") Long id, @RequestBody UdpatePatientCommand command){
        Patient patientForEdit = patientService.findById(id);
        Patient editedPatient = UdpatePatientCommand.toPatient(command, patientForEdit);
        Patient savedPatient = patientService.save(editedPatient);
        return PatientDto.fromPatient(savedPatient);
    }

    @PatchMapping("/{id}")
    private PatientDto editPartially(@PathVariable("id") Long id, @RequestBody UdpatePatientCommand command){
        Patient editedPatient = patientService.editPartially(id, command);
        return PatientDto.fromPatient(editedPatient);
    }



}
