package com.powtorka.vetclinic.model.patient;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Size(min = 2, message = "Name must have at least 2 characters")
    private String name;
    private String species;
    private String breed;
    private String ownerName;
    @Email(message = "Wrong email pattern. Check it once again")
    private String ownerEmail;
    @Min(value = 0, message = "Age must be greater or equal to 0")
    @Max(value = 1000, message = "Age can not be greater than 1000")
    private Integer age;
}