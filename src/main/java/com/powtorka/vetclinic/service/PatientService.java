package com.powtorka.vetclinic.service;

import com.powtorka.vetclinic.exceptions.PatientNotFoundException;
import com.powtorka.vetclinic.model.patient.UdpatePatientCommand;
import com.powtorka.vetclinic.model.patient.Patient;
import com.powtorka.vetclinic.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;

    public Patient findById(long id){
        return patientRepository.findById(id).orElseThrow(PatientNotFoundException::new);
    }

    public Patient save(Patient patient) {
        return patientRepository.save(patient);
    }

    public Page<Patient> findAll(Pageable pageable) {
        return patientRepository.findAll(pageable);
    }

    public void deleteById(long id){
        if(patientRepository.existsById(id)){
            patientRepository.deleteById(id);
        } else {
            throw new PatientNotFoundException(String.format("Patient with id: %s not found!", id));
        }
    }

    @Transactional
    public Patient editPatient(long id, UdpatePatientCommand command){
        return patientRepository.findById(id)
                .map(patient -> {
                    patient.setName(command.getName());
                    patient.setSpecies(command.getSpecies());
                    patient.setBreed(command.getBreed());
                    patient.setOwnerName(command.getOwnerName());
                    patient.setOwnerEmail(command.getOwnerEmail());
                    patient.setAge(command.getAge());
                    return patient;
                }).orElseThrow(() -> new PatientNotFoundException(String.format("Patient with id: %s not found", id)));
    }

    @Transactional
    public Patient editPartially(Long id, UdpatePatientCommand command){
        return patientRepository.findById(id)
                .map(patient -> {
                    Optional.ofNullable(command.getName()).ifPresent(patient::setName);
                    Optional.ofNullable(command.getSpecies()).ifPresent(patient::setSpecies);
                    Optional.ofNullable(command.getBreed()).ifPresent(patient::setBreed);
                    Optional.ofNullable(command.getOwnerName()).ifPresent(patient::setOwnerName);
                    Optional.ofNullable(command.getOwnerEmail()).ifPresent(patient::setOwnerEmail);
                    Optional.of(command.getAge()).ifPresent(patient::setAge);
                    return patient;
                }).orElseThrow(()->new PatientNotFoundException(String.format("Patient with id: %s not found", id)));
    }

    public List<Patient> findPatientsWithAgeGreaterThan(int age){
        return patientRepository.findByAgeGreaterThan(age);
    }



}
