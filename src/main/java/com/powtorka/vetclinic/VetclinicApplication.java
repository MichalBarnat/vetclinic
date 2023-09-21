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

	// 07092023 DONE
	// - wywalic metody toDoctor itp z commandow/dto - wszystkie konwersje maja sie odbywac przez modelmapper
	// - ogarnac walidacje danych wejsciowych https://www.baeldung.com/java-validation
	// - posprzatac w serwisach metody do edycji - zadnych 50 ifow, tylko optionale
	// - paginacja i sortowanie w patient i appointment
	// - wzorujac sie na TeacherControllerIT porobicie testy z uwzglednieniem paginacji i sortowania. Moze byc konieczne
	//   rozbudowanie plikow csv z przykladowymi danymi aby testy mialy sens

	// 14092023 DONE
	// 1) sprobowac przeniesc walidacje do commandow - zamiast w entity (TYLKO W ENTITY DZIALA)
	// 2) stworzyc logike weryfikacji, czy dany pacjent i lekarz nie probuja ustawic sobie wiecej niz jednej wizyty
	// 		w tym samym czasie. Zalozmy, ze jedna wizyta trwa 15 minut. To znaczy, ze jesli probujemy stworzyc nowa wizyte,
	// 		musimy sprawdzic, czy ani lekarz, ani pacjent nie maja juz w tym samym czasie wizyty,
	// 		ktora zaczynalaby sie do 15 minut przed tworzoną wizyta

	// TODO 21092023
	// liquibase dodac do glownej aplikacji dane testowe
	// testy Service Mockowanie!! when itp
	// dokonczyc Appointment
	//


}
