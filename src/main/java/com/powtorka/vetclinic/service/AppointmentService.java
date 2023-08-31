package com.powtorka.vetclinic.service;

import com.powtorka.vetclinic.model.appointment.Appointment;
import com.powtorka.vetclinic.model.appointment.UpdateAppointementCommand;
import com.powtorka.vetclinic.model.doctor.Doctor;
import com.powtorka.vetclinic.model.doctor.UpdateDoctorCommand;
import com.powtorka.vetclinic.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;

    public Appointment findById(long id) { return appointmentRepository.findById(id).orElseThrow();}

    public Appointment save(Appointment appointment) { return appointmentRepository.save(appointment);}

    public List<Appointment> findAll(){ return  appointmentRepository.findAll();}

    public void deleteById(long id){ appointmentRepository.deleteById(id);}

    public Appointment editPartially(Long id, UpdateAppointementCommand command) {
        Appointment appointmentForEdit = findById(id);
        if (command.getDoctorId() != null) {
            appointmentForEdit.getDoctor().setId(command.getDoctorId());
        }
        if (command.getPatientId() != null) {
            appointmentForEdit.getPatient().setId(command.getPatientId());
        }
        if (command.getDateTime() != null) {
            appointmentForEdit.setDateTime(command.getDateTime());
        }
        if (command.getPrice() != 0.0) {
            appointmentForEdit.setPrice(command.getPrice());
        }
        return save(appointmentForEdit);
    }
}
