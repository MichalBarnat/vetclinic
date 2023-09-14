package com.powtorka.vetclinic.repository;

import com.powtorka.vetclinic.model.appointment.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Page<Appointment> findAll(Pageable pageable);
//    @Query(select * from blabla where patientId = ?)
    List<Appointment> findAllByPatientId(Long patientId);
    List<Appointment> findAllByDoctorId(Long doctorId);

//    List<Appointment> findByPriceMoreExpensiveThan(int price);
}
