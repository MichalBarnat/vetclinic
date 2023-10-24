package com.powtorka.vetclinic.service;

import com.powtorka.vetclinic.exceptions.PatientNotFoundException;
import com.powtorka.vetclinic.model.patient.command.UdpatePatientCommand;
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

    public Patient findById(long id) {
        return patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException(String.format("Patient with id: %s not found!", id)));
    }

    public Patient save(Patient patient) {
        return patientRepository.save(patient);
    }

    public Page<Patient> findAll(Pageable pageable) {
        return patientRepository.findAll(pageable);
    }

    public void deleteById(long id) {
        if (patientRepository.existsById(id)) {
            patientRepository.deleteById(id);
        } else {
            throw new PatientNotFoundException(String.format("Patient with id: %s not found!", id));
        }
    }

    public void deleteAll() {
        patientRepository.deleteAll();
    }

    @Transactional
    public Patient editPatient(long id, UdpatePatientCommand command) {
        return patientRepository.findById(id)
                .map(patientToEdit -> {
                    patientToEdit.setName(command.getName());
                    patientToEdit.setSpecies(command.getSpecies());
                    patientToEdit.setBreed(command.getBreed());
                    patientToEdit.setOwnerName(command.getOwnerName());
                    patientToEdit.setOwnerEmail(command.getOwnerEmail());
                    patientToEdit.setAge(command.getAge());
                    return patientToEdit;
                }).orElseThrow(() -> new PatientNotFoundException(String.format("Patient with id: %s not found", id)));
    }

    @Transactional
    public Patient editPartially(Long id, UdpatePatientCommand command) {
        return patientRepository.findById(id)
                .map(patientToEdit -> {
                    Optional.ofNullable(command.getName()).ifPresent(patientToEdit::setName);
                    Optional.ofNullable(command.getSpecies()).ifPresent(patientToEdit::setSpecies);
                    Optional.ofNullable(command.getBreed()).ifPresent(patientToEdit::setBreed);
                    Optional.ofNullable(command.getOwnerName()).ifPresent(patientToEdit::setOwnerName);
                    Optional.ofNullable(command.getOwnerEmail()).ifPresent(patientToEdit::setOwnerEmail);
                    Optional.ofNullable(command.getAge()).ifPresent(patientToEdit::setAge);
                    return patientToEdit;
                }).orElseThrow(() -> new PatientNotFoundException(String.format("Patient with id: %s not found", id)));
    }

    public List<Patient> findPatientsWithAgeGreaterThan(int age) {
        return patientRepository.findByAgeGreaterThan(age);
    }


}
