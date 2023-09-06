package com.powtorka.vetclinic.service;

import com.powtorka.vetclinic.exceptions.DoctorWithThisIdDoNotExistException;
import com.powtorka.vetclinic.model.doctor.Doctor;
import com.powtorka.vetclinic.model.doctor.UpdateDoctorCommand;
import com.powtorka.vetclinic.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public Doctor findById(long id) {
        return doctorRepository.findById(id).orElseThrow(DoctorWithThisIdDoNotExistException::new);
    }

    public Doctor save(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }

    public void deleteById(long id) {
        doctorRepository.deleteById(id);
    }

    public Doctor editPartially(Long id, UpdateDoctorCommand command) {
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
