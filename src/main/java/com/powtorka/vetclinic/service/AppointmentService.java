package com.powtorka.vetclinic.service;

import com.powtorka.vetclinic.exceptions.AppointmentIsNotAvailableException;
import com.powtorka.vetclinic.exceptions.AppointmentNotFoundException;
import com.powtorka.vetclinic.model.appointment.Appointment;
import com.powtorka.vetclinic.model.appointment.AppointmentDto;
import com.powtorka.vetclinic.model.appointment.CreateAppointmentCommand;
import com.powtorka.vetclinic.model.appointment.UpdateAppointementCommand;
import com.powtorka.vetclinic.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;
    private final DoctorService doctorService;
    private final PatientService patientService;

    public Appointment findById(long id) {
        return appointmentRepository.findById(id).orElseThrow(AppointmentNotFoundException::new);
    }

    public Appointment save(CreateAppointmentCommand command) {
        if (appointmentIsAvailable(command)) {
            Appointment appointment = modelMapper.map(command, Appointment.class);
            return appointmentRepository.save(appointment);
        } else {
            throw new AppointmentIsNotAvailableException("Appointment is not available at: " + command.getDateTime());
        }
    }


    public Page<Appointment> findAll(Pageable pageable) {
        return appointmentRepository.findAll(pageable);
    }

    public void deleteById(long id) {
        appointmentRepository.deleteById(id);
    }

    @Transactional
    public Appointment editAppointment(long id, UpdateAppointementCommand command) {
        return appointmentRepository.findById(id)
                .map(appointmentToEdit -> {
                    if(command.getDoctorId() != null) {
                        appointmentToEdit.setDoctor(doctorService.findById(command.getDoctorId()));
                    }
                    if(command.getPatientId() != null) {
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
                    Optional.ofNullable(doctorService.findById(command.getDoctorId())).ifPresent(appointmentForEdit::setDoctor);
                    Optional.ofNullable(patientService.findById(command.getPatientId())).ifPresent(appointmentForEdit::setPatient);
                    Optional.ofNullable(command.getDateTime()).ifPresent(appointmentForEdit::setDateTime);
                    Optional.ofNullable(command.getPrice()).ifPresent(appointmentForEdit::setPrice);
                    return appointmentForEdit;
                })
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment with this id not found!"));
    }

    private boolean appointmentIsAvailable(CreateAppointmentCommand command) {
        LocalDateTime proposedDateTime = command.getDateTime();
        LocalDateTime proposedEndDateTime = proposedDateTime.plusMinutes(15);

        List<Appointment> doctorAppointments = appointmentRepository.findAllByDoctorId(command.getDoctorId());
        List<Appointment> patientAppointments = appointmentRepository.findAllByPatientId(command.getPatientId());

        boolean doctorTimeValidation = doctorAppointments.stream()
                .anyMatch(appointment -> {
                    LocalDateTime appointmentStart = appointment.getDateTime();
                    LocalDateTime appointmentEnd = appointmentStart.plusMinutes(15);
                    return (proposedDateTime.isBefore(appointmentEnd) && proposedEndDateTime.isAfter(appointmentStart));
                });

        boolean patientTimeValidation = patientAppointments.stream()
                .anyMatch(appointment -> {
                    LocalDateTime appointmentStart = appointment.getDateTime();
                    LocalDateTime appointmentEnd = appointmentStart.plusMinutes(15); // Czas końcowy istniejącego spotkania
                    return (proposedDateTime.isBefore(appointmentEnd) && proposedEndDateTime.isAfter(appointmentStart));
                });

        return !doctorTimeValidation && !patientTimeValidation;

    }

}
