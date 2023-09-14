package com.powtorka.vetclinic.mappings.doctor;

import com.powtorka.vetclinic.exceptions.DoctorNotFoundException;
import com.powtorka.vetclinic.model.doctor.Doctor;
import com.powtorka.vetclinic.model.doctor.UpdateDoctorCommand;
import com.powtorka.vetclinic.repository.DoctorRepository;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UpdateDoctorCommandToDoctorConverter implements Converter<UpdateDoctorCommand, Doctor> {

    private final DoctorRepository doctorRepository;

    public UpdateDoctorCommandToDoctorConverter(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

//    @Override
//    public Doctor convert(MappingContext<UpdateDoctorCommand, Doctor> mappingContext) {
//        UpdateDoctorCommand command = mappingContext.getSource();
//        Long doctorId = mappingContext.getDestination().getId();
//
//        return doctorRepository.findById(doctorId)
//                .map(doctorForEdit -> {
//                    Optional.ofNullable(command.getName()).ifPresent(doctorForEdit::setName);
//                    Optional.ofNullable(command.getSurname()).ifPresent(doctorForEdit::setSurname);
//                    Optional.ofNullable(command.getSpeciality()).ifPresent(doctorForEdit::setSpeciality);
//                    Optional.ofNullable(command.getAnimalSpeciality()).ifPresent(doctorForEdit::setAnimalSpeciality);
//                    Optional.ofNullable(command.getEmail()).ifPresent(doctorForEdit::setEmail);
//                    Optional.of(command.getRate()).ifPresent(doctorForEdit::setRate);
//                    Optional.ofNullable(command.getPesel()).ifPresent(doctorForEdit::setPesel);
//                    return doctorForEdit;
//                }).orElseThrow(() -> new DoctorNotFoundException(String.format("Doctor with id: %s not found!", doctorId)));
//    }

    @Override
    public Doctor convert(MappingContext<UpdateDoctorCommand, Doctor> mappingContext) {
        UpdateDoctorCommand command = mappingContext.getSource();

        return Doctor.builder()
                .name(command.getName())
                .surname(command.getSurname())
                .speciality(command.getSpeciality())
                .animalSpeciality(command.getAnimalSpeciality())
                .email(command.getEmail())
                .rate(command.getRate())
                .pesel(command.getPesel())
                .build();
    }

}
