package com.powtorka.vetclinic.model.doctor;

import lombok.*;

@Getter
@Setter // czy mozna? (do testowania potrzebny)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateDoctorCommand {
    private String name;
    private String surname;
    private String speciality;
    private String animalSpeciality;
    private String email;
    private int rate;
    private String pesel;

    public static Doctor toDoctor(UpdateDoctorCommand command, Doctor existingDoctor) {
        existingDoctor.setName(command.getName());
        existingDoctor.setSurname(command.getSurname());
        existingDoctor.setSpeciality(command.getSpeciality());
        existingDoctor.setAnimalSpeciality(command.getAnimalSpeciality());
        existingDoctor.setEmail(command.getEmail());
        existingDoctor.setRate(command.getRate());
        existingDoctor.setPesel(command.getPesel());
        return existingDoctor;
    }
}
