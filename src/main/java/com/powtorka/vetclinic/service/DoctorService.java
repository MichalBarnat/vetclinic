package com.powtorka.vetclinic.service;

import com.powtorka.vetclinic.exceptions.DoctorNotFoundException;
import com.powtorka.vetclinic.model.doctor.Doctor;
import com.powtorka.vetclinic.model.doctor.UpdateDoctorCommand;
import com.powtorka.vetclinic.repository.DoctorRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        if (doctorRepository.existsById(id)) {
            doctorRepository.deleteById(id);
        } else {
            throw new DoctorNotFoundException(String.format("Doctor with id: %s not found!", id));
        }
    }

    public void deleteAll() {
        doctorRepository.deleteAll();
    }

    @Transactional
    public Doctor editDoctor(long id, UpdateDoctorCommand command) {
        return doctorRepository.findById(id)
                .map(doctorToEdit -> {
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

    @Transactional
    public Doctor editPartially(Long id, UpdateDoctorCommand command) {
        return doctorRepository.findById(id)
                .map(doctorForEdit -> {
                    Optional.ofNullable(command.getName()).ifPresent(doctorForEdit::setName);
                    Optional.ofNullable(command.getSurname()).ifPresent(doctorForEdit::setSurname);
                    Optional.ofNullable(command.getSpeciality()).ifPresent(doctorForEdit::setSpeciality);
                    Optional.ofNullable(command.getAnimalSpeciality()).ifPresent(doctorForEdit::setAnimalSpeciality);
                    Optional.ofNullable(command.getEmail()).ifPresent(doctorForEdit::setEmail);
                    Optional.ofNullable(command.getRate()).ifPresent(doctorForEdit::setRate);
                    Optional.ofNullable(command.getPesel()).ifPresent(doctorForEdit::setPesel);
                    return doctorForEdit;
                }).orElseThrow(() -> new DoctorNotFoundException(String.format("Doctor with id: %s not found!", id)));
    }

    public List<Doctor> findDoctorsWithRateGreaterThan(int rate) {
        return doctorRepository.findByRateGreaterThan(rate);
    }


}
