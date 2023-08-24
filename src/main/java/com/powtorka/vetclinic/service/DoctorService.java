package com.powtorka.vetclinic.service;

import com.powtorka.vetclinic.model.doctor.Doctor;
import com.powtorka.vetclinic.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public Doctor findById(long id) {
        return doctorRepository.findById(id).orElseThrow();
    }

    public Doctor save(Doctor doctor) {
        return doctorRepository.save(doctor);
    }
}
