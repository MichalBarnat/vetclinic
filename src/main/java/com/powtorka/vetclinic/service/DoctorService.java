package com.powtorka.vetclinic.service;

import com.powtorka.vetclinic.exceptions.DoctorNotFoundException;
import com.powtorka.vetclinic.model.doctor.Doctor;
import com.powtorka.vetclinic.model.doctor.UpdateDoctorCommand;
import com.powtorka.vetclinic.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public Doctor findById(long id) {
        return doctorRepository.findById(id).orElseThrow(() -> new DoctorNotFoundException(String.format("Doctor with id: %s not found!", id)));
    }

    public Doctor save(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public Page<Doctor> findAll(Pageable pageable) {
        return doctorRepository.findAll(pageable);
    }

    public void deleteById(long id) {
        if(doctorRepository.existsById(id)) {
            doctorRepository.deleteById(id);
        } else {
            throw new DoctorNotFoundException(String.format("Doctor with id: %s not found!", id));
        }
    }

    @Transactional
    public Doctor editTeacher(long id, UpdateDoctorCommand command) {
        return doctorRepository.findById(id)
                .map(doctorToEdit -> {
                    // TODO ponizsze mozna wykorzystac w edit partially, ale na poczatek dawac Optional.ofNullable()
                    doctorToEdit.setName(command.getName());
                    doctorToEdit.setSurname(command.getSurname());
                    doctorToEdit.setSpeciality(command.getSpeciality());
                    doctorToEdit.setAnimalSpeciality(command.getAnimalSpeciality());
                    doctorToEdit.setEmail(command.getEmail());
                    doctorToEdit.setRate(command.getRate());
                    doctorToEdit.setPesel(command.getPesel());
                    return doctorToEdit;
                }).orElseThrow(() -> new DoctorNotFoundException(String.format("Doctor with id: %s not found!", id)));
    }


    public Doctor editPartially(Long id, UpdateDoctorCommand command) {
        // TODO optionale!
        Doctor doctorForEdit = findById(id);
        if (command.getName() != null) {
            doctorForEdit.setName(command.getName());
        }
        if (command.getSurname() != null) {
            doctorForEdit.setSurname(command.getSurname());
        }
        if (command.getSpeciality() != null) {
            doctorForEdit.setSpeciality(command.getSpeciality());
        }
        if (command.getAnimalSpeciality() != null) {
            doctorForEdit.setAnimalSpeciality(command.getAnimalSpeciality());
        }
        if (command.getEmail() != null) {
            doctorForEdit.setEmail(command.getEmail());
        }
        if (command.getRate() > 0) {
            doctorForEdit.setRate(command.getRate());
        }
        if (command.getPesel() != null) {
            doctorForEdit.setPesel(command.getPesel());
        }
        return save(doctorForEdit);
    }


}
