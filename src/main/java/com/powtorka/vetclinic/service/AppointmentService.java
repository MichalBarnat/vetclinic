package com.powtorka.vetclinic.service;

import com.powtorka.vetclinic.exceptions.AppointmentWithThisIdDoNotExistException;
import com.powtorka.vetclinic.model.appointment.Appointment;
import com.powtorka.vetclinic.model.appointment.AppointmentDto;
import com.powtorka.vetclinic.model.appointment.CreateAppointmentCommand;
import com.powtorka.vetclinic.model.appointment.UpdateAppointementCommand;
import com.powtorka.vetclinic.model.doctor.Doctor;
import com.powtorka.vetclinic.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return appointmentRepository.findById(id).orElseThrow(AppointmentWithThisIdDoNotExistException::new);
    }

    public AppointmentDto save(CreateAppointmentCommand command) {
        Appointment appointment = modelMapper.map(command, Appointment.class);
        Appointment savedAppointment =  appointmentRepository.save(appointment);
        return modelMapper.map(savedAppointment, AppointmentDto.class);
    }


    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    public void deleteById(long id) {
        appointmentRepository.deleteById(id);
    }

    @Transactional
    public Appointment editAppointment(long id, UpdateAppointementCommand command){
        return  appointmentRepository.findById(id)
                .map(appointmentToEdit -> {
                    appointmentToEdit.setDoctor(doctorService.findById(command.getPatientId()));
                    appointmentToEdit.setPatient(patientService.findById(command.getPatientId()));
                    appointmentToEdit.setDateTime(command.getDateTime());
                    appointmentToEdit.setPrice(command.getPrice());
                    return appointmentToEdit;
                } ).orElseThrow(() -> new AppointmentWithThisIdDoNotExistException(String.format("Appointment with this id not found!")));
    }

    public Appointment editPartielly(Long id, UpdateAppointementCommand command) {
        return appointmentRepository.findById(id)
                .map(appointmentForEdit -> {
                    Optional.ofNullable(doctorService.findById(command.getDoctorId())).ifPresent(appointmentForEdit::setDoctor);
                    Optional.ofNullable(patientService.findById(command.getPatientId())).ifPresent(appointmentForEdit::setPatient);
                    Optional.ofNullable(command.getDateTime()).ifPresent(appointmentForEdit::setDateTime);
                    Optional.ofNullable(command.getPrice()).ifPresent(appointmentForEdit::setPrice);
                    return appointmentForEdit;
                })
                .orElseThrow(() -> new AppointmentWithThisIdDoNotExistException("Appointment with this id not found!"));
    }

}
