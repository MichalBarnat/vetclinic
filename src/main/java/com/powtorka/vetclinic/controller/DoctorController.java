package com.powtorka.vetclinic.controller;

import com.powtorka.vetclinic.model.doctor.*;
import com.powtorka.vetclinic.model.doctor.command.CreateDoctorCommand;
import com.powtorka.vetclinic.model.doctor.command.CreateDoctorPageCommand;
import com.powtorka.vetclinic.model.doctor.command.UpdateDoctorCommand;
import com.powtorka.vetclinic.model.doctor.dto.DoctorDto;
import com.powtorka.vetclinic.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/doctor")
public class DoctorController {

    private final DoctorService doctorService;
    private final ModelMapper modelMapper;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<DoctorDto> findById(@PathVariable("id") Long id) {
        Doctor doctor = doctorService.findById(id);
        if(doctor == null) {
            return ResponseEntity.status(NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(modelMapper.map(doctor, DoctorDto.class));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<DoctorDto> save(@RequestBody CreateDoctorCommand command) {
        Doctor toSave = modelMapper.map(command, Doctor.class);
        Doctor savedDoctor = doctorService.save(toSave);
        return new ResponseEntity<>(modelMapper.map(savedDoctor, DoctorDto.class), CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<DoctorDto>> findAll(CreateDoctorPageCommand command) {

        Page<Doctor> page = doctorService.findAll(modelMapper.map(command, Pageable.class));

        List<DoctorDto> list = page.stream()
                .map(doctor -> modelMapper.map(doctor, DoctorDto.class))
                .toList();
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Long id) {
        doctorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deleteAll")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAll() {
        doctorService.deleteAll();
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<DoctorDto> edit(@PathVariable("id") Long id, @RequestBody UpdateDoctorCommand command) {
        Doctor editedDoctor = doctorService.editDoctor(id, command);
        return ResponseEntity.ok(modelMapper.map(editedDoctor, DoctorDto.class));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<DoctorDto> editPartially(@PathVariable("id") Long id, @RequestBody UpdateDoctorCommand command) {
        Doctor editedDoctor = doctorService.editPartially(id, command);
        return ResponseEntity.ok(modelMapper.map(editedDoctor, DoctorDto.class));
    }

    @GetMapping("/top-rated")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<Doctor>> getTopRatedDoctors(@RequestParam(name = "minRate", required = false, defaultValue = "80") int minRate) {
        List<Doctor> topRatedDoctors = doctorService.findDoctorsWithRateGreaterThan(minRate);
        return ResponseEntity.ok(topRatedDoctors);
    }

    @GetMapping("/testRead")
    @PreAuthorize("hasAuthority('DOCTOR_READ')")
    public ResponseEntity<String> testRead() {
        String hello = "hello READ TEST";
        return ResponseEntity.ok(hello);
    }

    @GetMapping("/testWrite")
    @PreAuthorize("hasAuthority('DOCTOR_WRITE')")
    public ResponseEntity<String> testWrite() {
        String hello = "hello WRITE TEST";
        return ResponseEntity.ok(hello);
    }


}
