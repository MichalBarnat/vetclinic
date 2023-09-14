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
        } catch (PatientNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping
    public PatientDto save(@RequestBody CreatePatientCommand command) {
        // TODO krystian popraw:
        Patient toSave = CreatePatientCommand.toPatient(command);
        Patient savedPatient = patientService.save(toSave);
        return PatientDto.fromPatient(savedPatient);
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
