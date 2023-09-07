package com.powtorka.vetclinic.service;

import com.powtorka.vetclinic.exceptions.AppointmentWithThisIdDoNotExistException;
import com.powtorka.vetclinic.model.appointment.Appointment;
import com.powtorka.vetclinic.model.appointment.AppointmentDto;
import com.powtorka.vetclinic.model.appointment.CreateAppointmentCommand;
import com.powtorka.vetclinic.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final ModelMapper modelMapper;

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

//    public Appointment editPartially(Long id, UpdateAppointementCommand command) {
//        Appointment appointmentForEdit = findById(id);
//        if (command.getDoctorId() != null) {
//            appointmentForEdit.getDoctor().setId(command.getDoctorId());
//        }
//        if (command.getPatientId() != null) {
//            appointmentForEdit.getPatient().setId(command.getPatientId());
//        }
//        if (command.getDateTime() != null) {
//            appointmentForEdit.setDateTime(command.getDateTime());
//        }
//        if (command.getPrice() != 0.0) {
//            appointmentForEdit.setPrice(command.getPrice());
//        }
//        return save(appointmentForEdit);
//    }
}
