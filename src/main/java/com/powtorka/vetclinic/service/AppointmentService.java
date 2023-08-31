package com.powtorka.vetclinic.service;

import com.powtorka.vetclinic.model.appointment.Appointment;
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
}
