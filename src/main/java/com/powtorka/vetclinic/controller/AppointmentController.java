package com.powtorka.vetclinic.controller;

import com.powtorka.vetclinic.model.appointment.Appointment;
import com.powtorka.vetclinic.model.appointment.AppointmentDto;
import com.powtorka.vetclinic.model.appointment.CreateAppointmentCommand;
import com.powtorka.vetclinic.model.appointment.UpdateAppointementCommand;
import com.powtorka.vetclinic.model.doctor.CreateDoctorCommand;
import com.powtorka.vetclinic.model.doctor.Doctor;
import com.powtorka.vetclinic.model.doctor.DoctorDto;
import com.powtorka.vetclinic.model.doctor.UpdateDoctorCommand;
import com.powtorka.vetclinic.repository.DoctorRepository;
import com.powtorka.vetclinic.repository.PatientRepository;
import com.powtorka.vetclinic.service.AppointmentService;
import com.powtorka.vetclinic.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/appointment")
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;

    @GetMapping("/{id}")
    private AppointmentDto findById(@PathVariable("id") Long id) {
        Appointment appointment = appointmentService.findById(id);
        return AppointmentDto.fromAppointment(appointment);
    }

    @PostMapping
    public AppointmentDto save(@RequestBody CreateAppointmentCommand command) {
        Appointment toSave = CreateAppointmentCommand.toAppointment(command, doctorRepository,patientRepository,modelMapper);
        Appointment savedAppointment = appointmentService.save(toSave);
        return AppointmentDto.fromAppointment(savedAppointment);
    }

    @GetMapping
    private List<AppointmentDto> findAll() {
        return appointmentService.findAll()
                .stream()
                .map(AppointmentDto::fromAppointment)
                .toList();
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
        appointmentService.deleteById(id);
        return ResponseEntity.ok("Appointment with ID: " + id + "has been deleted");
    }


}
