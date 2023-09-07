package com.powtorka.vetclinic.service;

import com.powtorka.vetclinic.exceptions.InvalidPatientAgeException;
import com.powtorka.vetclinic.exceptions.PatientWithThisIdDoNotExistException;
import com.powtorka.vetclinic.model.patient.UdpatePatientCommand;
import com.powtorka.vetclinic.model.patient.Patient;
import com.powtorka.vetclinic.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void deleteById(long id) {
        patientRepository.deleteById(id);
    }

    public Patient editPartially(Long id, UdpatePatientCommand command){
        Patient patientForEdit = findById(id);
        if (command.getName() != null){
            patientForEdit.setName(command.getName());
        }
        if (command.getBreed() != null){
            patientForEdit.setBreed(command.getBreed());
        }
        if (command.getOwnerName() != null){
            patientForEdit.setOwnerName(command.getOwnerName());
        }
        if (command.getOwnerEmail() != null){
            patientForEdit.setOwnerEmail(command.getOwnerEmail());
        }
        if (command.getAge() > 0){
            patientForEdit.setAge(command.getAge());
        }
        if (command.getAge() <= 0) {
            throw new InvalidPatientAgeException("Age must be a positive value");
        }
        patientForEdit.setAge(command.getAge());

        return save(patientForEdit);



    }



}
