package com.powtorka.vetclinic.controller;

import com.powtorka.vetclinic.model.doctor.*;
import com.powtorka.vetclinic.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/doctor")
public class DoctorController {

    private final DoctorService doctorService;
    private final ModelMapper modelMapper;

    //@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ADMIN')")
    @PreAuthorize("permitAll()")
    @GetMapping("/{id}")
    private ResponseEntity<DoctorDto> findById(@PathVariable("id") Long id) {
        Doctor doctor = doctorService.findById(id);
        if(doctor == null) {
            return ResponseEntity.status(NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(modelMapper.map(doctor, DoctorDto.class));
    }

    @PostMapping
    public ResponseEntity<DoctorDto> save(@RequestBody CreateDoctorCommand command) {
        Doctor toSave = modelMapper.map(command, Doctor.class);
        Doctor savedDoctor = doctorService.save(toSave);
        return new ResponseEntity<>(modelMapper.map(savedDoctor, DoctorDto.class), CREATED);
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ADMIN')")
    @GetMapping
    private ResponseEntity<List<DoctorDto>> findAll(CreateDoctorPageCommand command) {

        Page<Doctor> page = doctorService.findAll(modelMapper.map(command, Pageable.class));

        List<DoctorDto> list = page.stream()
                .map(doctor -> modelMapper.map(doctor, DoctorDto.class))
                .toList();
        return ResponseEntity.ok(list);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deleteById(@PathVariable("id") Long id) {
        doctorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteAll")
    private ResponseEntity<Void> deleteAll() {
        doctorService.deleteAll();
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    private ResponseEntity<DoctorDto> edit(@PathVariable("id") Long id, @RequestBody UpdateDoctorCommand command) {
        Doctor editedDoctor = doctorService.editDoctor(id, command);
        return ResponseEntity.ok(modelMapper.map(editedDoctor, DoctorDto.class));
    }

    @PatchMapping("/{id}")
    private ResponseEntity<DoctorDto> editPartially(@PathVariable("id") Long id, @RequestBody UpdateDoctorCommand command) {
        Doctor editedDoctor = doctorService.editPartially(id, command);
        return ResponseEntity.ok(modelMapper.map(editedDoctor, DoctorDto.class));
    }

    @GetMapping("/top-rated")
    private ResponseEntity<List<Doctor>> getTopRatedDoctors(@RequestParam(name = "minRate", required = false, defaultValue = "80") int minRate) {
        List<Doctor> topRatedDoctors = doctorService.findDoctorsWithRateGreaterThan(minRate);
        return ResponseEntity.ok(topRatedDoctors);
    }


}
