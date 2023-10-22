package com.powtorka.vetclinic.controller;

import com.powtorka.vetclinic.model.appointment.*;
import com.powtorka.vetclinic.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("/appointment")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final ModelMapper modelMapper;

    @GetMapping("/{id}")
    private ResponseEntity<AppointmentDto> findById(@PathVariable("id") Long id) {
        Appointment appointment = appointmentService.findById(id);
        if(appointment == null){
            return ResponseEntity.status(NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(modelMapper.map(appointment, AppointmentDto.class));
    }

    @PostMapping
    public ResponseEntity<AppointmentDto> save(@RequestBody CreateAppointmentCommand command) {
        Appointment toSave = modelMapper.map(command, Appointment.class);
        Appointment savedAppointment = appointmentService.save(toSave);
        return new ResponseEntity<>(modelMapper.map(savedAppointment, AppointmentDto.class), CREATED);
    }

    @GetMapping
    private ResponseEntity<List<AppointmentDto>> findAll(CreateAppointmentPageCommand command) {
        List<AppointmentDto> list = appointmentService.findAll(modelMapper.map(command, Pageable.class))
                .stream()
                .map(appointment -> modelMapper.map(appointment, AppointmentDto.class))
                .toList();
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deleteById(@PathVariable("id") Long id) {
        appointmentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deleteAll")
    private ResponseEntity<Void> deleteAll() {
        appointmentService.deleteAll();
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    private ResponseEntity<AppointmentDto> edit(@PathVariable("id") Long id, @RequestBody UpdateAppointementCommand command) {
        Appointment editedAppointment = appointmentService.editAppointment(id, command);
        return ResponseEntity.ok(modelMapper.map(editedAppointment, AppointmentDto.class));
    }

    @PatchMapping("/{id}")
    private ResponseEntity<AppointmentDto> editPartially(@PathVariable("id") Long id, @RequestBody UpdateAppointementCommand command) {
        Appointment editedAppointment = appointmentService.editPartially(id, command);
        return ResponseEntity.ok(modelMapper.map(editedAppointment, AppointmentDto.class));
    }

}
