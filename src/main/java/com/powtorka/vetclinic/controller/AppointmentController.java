package com.powtorka.vetclinic.controller;

import com.powtorka.vetclinic.mappings.appointment.AppointmentToAppointmentDtoConverter;

import com.powtorka.vetclinic.model.appointment.*;
import com.powtorka.vetclinic.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/appointment")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final ModelMapper modelMapper;
    private final AppointmentToAppointmentDtoConverter appointmentToAppointmentDtoConverter;

    @GetMapping("/{id}")
    private AppointmentDto findById(@PathVariable("id") Long id) {
        Appointment appointment = appointmentService.findById(id);
        return modelMapper.map(appointment, AppointmentDto.class);
    }

    @PostMapping
    public AppointmentDto save(@RequestBody CreateAppointmentCommand command) {
        return appointmentService.save(command);
    }

    @GetMapping
    private List<AppointmentDto> findAll(CreateAppointmentPageCommand command) {
        System.out.println("TEST");
        System.out.println(appointmentService.findAll(modelMapper.map(command, Pageable.class)));
        return appointmentService.findAll(modelMapper.map(command, Pageable.class))
                .stream()
                .map(appointment -> modelMapper.map(appointment, AppointmentDto.class))
                .toList();
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
        appointmentService.deleteById(id);
        return ResponseEntity.ok("Appointment with ID: " + id + " has been deleted");
    }


    @PutMapping("/{id}")
    private AppointmentDto edit(@PathVariable("id") Long id, @RequestBody UpdateAppointementCommand command) {
        Appointment editedAppointment = appointmentService.editAppointment(id, command);
        return modelMapper.map(editedAppointment, AppointmentDto.class);
    }

    @PatchMapping("/{id}")
    private AppointmentDto editPartially(@PathVariable("id") Long id, @RequestBody UpdateAppointementCommand command) {
        Appointment editedAppointment = appointmentService.editPartially(id, command);
        return modelMapper.map(editedAppointment, AppointmentDto.class);
    }



}
