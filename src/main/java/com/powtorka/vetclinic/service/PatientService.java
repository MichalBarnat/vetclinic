package com.powtorka.vetclinic.service;

import com.powtorka.vetclinic.model.patient.Patient;
import com.powtorka.vetclinic.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;

    public Patient findById(long id) {
        return patientRepository.findById(id).orElseThrow();
    }

    public Patient save(Patient patient) {
        return patientRepository.save(patient);
    }

    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    public void deleteById(long id) {
        patientRepository.deleteById(id);
    }
}
