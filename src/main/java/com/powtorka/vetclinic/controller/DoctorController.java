package com.powtorka.vetclinic.controller;

import com.powtorka.vetclinic.model.doctor.*;
import com.powtorka.vetclinic.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/doctor")
public class DoctorController {

    private final DoctorService doctorService;
    private final ModelMapper modelMapper;

    @GetMapping("/{id}")
    private DoctorDto findById(@PathVariable("id") Long id) {
        Doctor doctor = doctorService.findById(id);
        return modelMapper.map(doctor, DoctorDto.class);
    }

    @PostMapping
    public DoctorDto save(@RequestBody CreateDoctorCommand command) {
        Doctor toSave = modelMapper.map(command, Doctor.class);
        Doctor savedDoctor = doctorService.save(toSave);
        return modelMapper.map(savedDoctor, DoctorDto.class);
    }

    @GetMapping
    private List<DoctorDto> findAll(CreateDoctorPageCommand command) {
        return doctorService.findAll(modelMapper.map(command, Pageable.class))
                .stream()
                .map(doctor -> modelMapper.map(doctor, DoctorDto.class))
                .toList();
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
        doctorService.deleteById(id);
        return ResponseEntity.ok("Doctor with ID: " + id + " has been deleted");
    }

    @PutMapping("/{id}")
    private DoctorDto edit(@PathVariable("id") Long id, @RequestBody UpdateDoctorCommand command) {
        Doctor editedDoctor = doctorService.editDoctor(id, command);
        return modelMapper.map(editedDoctor, DoctorDto.class);
    }

    @PatchMapping("/{id}")
    private DoctorDto editPartially(@PathVariable("id") Long id, @RequestBody UpdateDoctorCommand command) {
        Doctor editedDoctor = doctorService.editPartially(id, command);
        return modelMapper.map(editedDoctor, DoctorDto.class);
    }

    @GetMapping("/top-rated")
    private ResponseEntity<List<Doctor>> getTopRatedDoctors(@RequestParam(name = "minRate", required = false, defaultValue = "80") int minRate) {
        List<Doctor> topRatedDoctors = doctorService.findDoctorsWithRateGreaterThan(minRate);
        return ResponseEntity.ok(topRatedDoctors);
    }


}
