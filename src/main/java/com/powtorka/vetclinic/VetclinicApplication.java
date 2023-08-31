package com.powtorka.vetclinic;

import com.powtorka.vetclinic.model.appointment.UpdateAppointementCommand;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VetclinicApplication {

	public static void main(String[] args) {
		SpringApplication.run(VetclinicApplication.class, args);
	}

	// TODO projekt aplikacji
	// w modelu znajda sie 3 encje - Doctor, Patient, Appointment
	// Doctor ma pola id, name, surname, specialty, animalSpecialty, email, rate, PESEL
	// Patient ma pola id, name, species, breed, ownerName, ownerEmail, age
	// Appointment ma id, pola doctor, patient, dateTime, price
	// modele maja tez posiadac swoje klasy Dto oraz commandy - createXCommand, editXCommand

	// commandy od appointment nie maja zawierac lekarza i pacjenta, a ich id - potrzebny bedzie converter na bazie modelMappera (wyszukac w mvnRepository)
	// w ktorym znajdziecie doctora i patienta po ich id przez ich serwisy

	// aplikacja ma byc tzw CRUDem REST - crud to inaczej create/read/update/delete
	// ma posiadac warstwy controller - tu dajemy same tzw endpointy, koncowki - kazdy ma miec nastepujace koncowki:
	// findAll - zwraca wszystkie obiekty danego typu @GetMapping
	// findById @GetMapping
	// save @Postmapping
	// delete @DeleteMapping
	// edit @Putmapping
	// editPartially @PatchMapping

	// controller ma wylacznei przenosic do serwisu - nie moze w nim byc tzw logiki biznesowej

	// Michal robi Doctor
	// Krystian robi Patient
	// Mateusz robi Appointment

	// po stworzeniu powyzszego sprobujcie potestowac swoje odpowiednie serwisy



}
