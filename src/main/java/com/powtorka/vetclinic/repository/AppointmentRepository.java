package com.powtorka.vetclinic.repository;

import com.powtorka.vetclinic.model.appointment.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}
