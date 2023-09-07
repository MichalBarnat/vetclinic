package com.powtorka.vetclinic.controller;

import com.powtorka.vetclinic.model.appointment.Appointment;
import com.powtorka.vetclinic.model.appointment.AppointmentDto;
import com.powtorka.vetclinic.model.appointment.CreateAppointmentCommand;
import com.powtorka.vetclinic.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/appointment")
public class AppointmentController {
    private final AppointmentService appointmentService;

    @GetMapping("/{id}")
    private AppointmentDto findById(@PathVariable("id") Long id) {
        Appointment appointment = appointmentService.findById(id);
        return AppointmentDto.fromAppointment(appointment);
    }

    @PostMapping
    public AppointmentDto save(@RequestBody CreateAppointmentCommand command) {
        return appointmentService.save(command);
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

    /*
    @PutMapping("/{id}")
    private AppointmentDto edit(@PathVariable("id") Long id, @RequestBody UpdateAppointementCommand command) {
        Appointment appointmentForEdit = appointmentService.findById(id);
        Appointment editedAppointment = UpdateAppointementCommand.toAppointment(command, appointmentForEdit);
        Appointment savedAppointment = appointmentService.save(editedAppointment);
        return AppointmentDto.fromAppointment(savedAppointment);
    }

    @PatchMapping{"/{id}"}
    private AppointmentDto editPartially(@PathVariable("id") Long id, @RequestBody UpdateAppointementCommand command) {
        Appointment editedAppointment = appointmentService.editPartially(id,command);
        return AppointmentDto.fromAppointment(editedAppointment);
    }

     */

}
