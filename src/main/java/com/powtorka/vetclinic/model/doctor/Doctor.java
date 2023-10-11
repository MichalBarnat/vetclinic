package com.powtorka.vetclinic.model.doctor;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(min = 2, message = "Name must have at least 2 characters!")
    private String name;
    private String surname;
    private String speciality;
    private String animalSpeciality;
    @Email(message = "Wrong email pattern. Check it once again!")
    private String email;
    @Min(value = 0, message = "Rate must be greater or equal to 0!")
    @Max(value = 100, message = "Rate can not be greater than 100!")
    private Integer rate;
    @Pattern(regexp = "\\d{11}", message = "Pesel must have exactly 11 digits!")
    private String pesel;
}
