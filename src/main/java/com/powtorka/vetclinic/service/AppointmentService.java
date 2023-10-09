package com.powtorka.vetclinic.service;

import com.powtorka.vetclinic.exceptions.AppointmentIsNotAvailableExpcetion;
import com.powtorka.vetclinic.exceptions.AppointmentNotFoundException;
import com.powtorka.vetclinic.model.appointment.Appointment;
import com.powtorka.vetclinic.model.appointment.UpdateAppointementCommand;
import com.powtorka.vetclinic.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;

    public Appointment findById(long id) {
        return appointmentRepository.findById(id).orElseThrow(() -> new AppointmentNotFoundException(String.format("Appointment with id %d not found!", id)));
    }

    public Appointment save(Appointment appointment) {
        if (appointmentIsAvailable(appointment)) {
            return appointmentRepository.save(appointment);
        } else {
            throw new AppointmentIsNotAvailableExpcetion("Appointment is not available at: " + appointment.getDateTime());
        }
    }


    public Page<Appointment> findAll(Pageable pageable) {
        return appointmentRepository.findAll(pageable);
    }

    public void deleteById(long id) {
        appointmentRepository.deleteById(id);
    }

    public void deleteAll() {
        appointmentRepository.deleteAll();
    }

    @Transactional
    public Appointment editAppointment(long id, UpdateAppointementCommand command) {
        return appointmentRepository.findById(id)
                .map(appointmentToEdit -> {
                    if (command.getDoctorId() != null) {
                        appointmentToEdit.setDoctor(doctorService.findById(command.getDoctorId()));
                    }
                    if (command.getPatientId() != null) {
                        appointmentToEdit.setPatient(patientService.findById(command.getPatientId()));
                    }
                    appointmentToEdit.setDateTime(command.getDateTime());
                    appointmentToEdit.setPrice(command.getPrice());
                    return appointmentToEdit;
                }).orElseThrow(() -> new AppointmentNotFoundException(String.format("Appointment with this id not found!")));
    }

    @Transactional
    public Appointment editPartially(Long id, UpdateAppointementCommand command) {
        return appointmentRepository.findById(id)
                .map(appointmentForEdit -> {
                    if (command.getDoctorId() != null) {
                        Optional.ofNullable(doctorService.findById(command.getDoctorId())).ifPresent(appointmentForEdit::setDoctor);
                    }
                    if (command.getPatientId() != null) {
                        Optional.ofNullable(patientService.findById(command.getPatientId())).ifPresent(appointmentForEdit::setPatient);
                    }
                    Optional.ofNullable(command.getDateTime()).ifPresent(appointmentForEdit::setDateTime);
                    Optional.of(command.getPrice()).ifPresent(appointmentForEdit::setPrice);
                    return appointmentForEdit;
                })
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment with this id not found!"));
    }

    public boolean appointmentIsAvailable(Appointment appointment) {
        LocalDateTime proposedDateTime = appointment.getDateTime();
        LocalDateTime proposedEndDateTime = proposedDateTime.plusMinutes(15);

        List<Appointment> doctorAppointments = appointmentRepository.findAllByDoctorId(appointment.getDoctor().getId());
        List<Appointment> patientAppointments = appointmentRepository.findAllByPatientId(appointment.getPatient().getId());

        boolean doctorTimeValidation = doctorAppointments.stream()
                .anyMatch(appointm -> {
                    LocalDateTime appointmentStart = appointm.getDateTime();
                    LocalDateTime appointmentEnd = appointmentStart.plusMinutes(15);
                    return (proposedDateTime.isBefore(appointmentEnd) && proposedEndDateTime.isAfter(appointmentStart));
                });

        boolean patientTimeValidation = patientAppointments.stream()
                .anyMatch(appoint -> {
                    LocalDateTime appointmentStart = appoint.getDateTime();
                    LocalDateTime appointmentEnd = appointmentStart.plusMinutes(15);
                    return (proposedDateTime.isBefore(appointmentEnd) && proposedEndDateTime.isAfter(appointmentStart));
                });

        return !doctorTimeValidation && !patientTimeValidation;

    }

}
