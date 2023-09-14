package com.powtorka.vetclinic.service;

import com.powtorka.vetclinic.exceptions.InvalidPatientAgeException;
import com.powtorka.vetclinic.exceptions.PatientWithThisIdDoNotExistException;
import com.powtorka.vetclinic.model.patient.UdpatePatientCommand;
import com.powtorka.vetclinic.model.patient.Patient;
import com.powtorka.vetclinic.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;

    public Patient findById(long id){
        return patientRepository.findById(id).orElseThrow(PatientWithThisIdDoNotExistException::new);
    }

    public Patient save(Patient patient) {
        return patientRepository.save(patient);
    }

    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    public void deleteById(long id){
        if(patientRepository.existsById(id)){
            patientRepository.deleteById(id);
        } else {
            throw new PatientWithThisIdDoNotExistException(String.format("Patient with id: %s not found!", id));
        }
    }

//    public Patient editPartially(Long id, UdpatePatientCommand command){
//        Patient patientForEdit = findById(id);
//        if (command.getName() != null){
//            patientForEdit.setName(command.getName());
//        }
//        if (command.getBreed() != null){
//            patientForEdit.setBreed(command.getBreed());
//        }
//        if (command.getOwnerName() != null){
//            patientForEdit.setOwnerName(command.getOwnerName());
//        }
//        if (command.getOwnerEmail() != null){
//            patientForEdit.setOwnerEmail(command.getOwnerEmail());
//        }
//        if (command.getAge() > 0){
//            patientForEdit.setAge(command.getAge());
//        }
//        if (command.getAge() <= 0) {
//            throw new InvalidPatientAgeException("Age must be a positive value");
//        }
//        patientForEdit.setAge(command.getAge());
//
//        return save(patientForEdit);
//    }

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
                }).orElseThrow(() -> new PatientWithThisIdDoNotExistException(String.format("Patient with id: %s not found", id)));
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
                }).orElseThrow(()->new PatientWithThisIdDoNotExistException(String.format("Patient with id: %s not found", id)));
    }

    public List<Patient> findPatientsWithAgeGreaterThan(int age){
        return patientRepository.findByRateGreaterThan(age);
    }



}
