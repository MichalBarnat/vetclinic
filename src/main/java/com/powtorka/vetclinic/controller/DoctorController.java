package com.powtorka.vetclinic.controller;

import com.powtorka.vetclinic.model.doctor.CreateDoctorCommand;
import com.powtorka.vetclinic.model.doctor.Doctor;
import com.powtorka.vetclinic.model.doctor.DoctorDto;
import com.powtorka.vetclinic.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/doctor")
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping("/{id}")
    private DoctorDto findById(@PathVariable("id") Long id) {
        System.out.println("przed find");
         Doctor doctor = doctorService.findById(id);
        System.out.println("po find");
         return DoctorDto.fromDoctor(doctor);
    }

    @PostMapping
    public DoctorDto save(@RequestBody CreateDoctorCommand command) {
        Doctor toSave = CreateDoctorCommand.toDoctor(command);
        Doctor savedDoctor = doctorService.save(toSave);
        return DoctorDto.fromDoctor(savedDoctor);
    }
}
