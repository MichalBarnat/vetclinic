package com.powtorka.vetclinic;

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

	// 21092023
	// liquibase dodac do glownej aplikacji dane testowe
	// testy Service Mockowanie!! when itp
	// dokonczyc Appointment
	//

	//05.10.2023
	//testy saved poprawic wedlug doctora
	//zmienic metody na requesty z poprawnymi kodami
	//JACOCO - oczekuje pokrycia kodu testami minimum 80% - zarowno instructions, jak i branches

	// kazdy: zainstalowac Docker Desktop
	// postawic baze na dockerze, niech to bedzie PostgreSQL wg instrukcji: https://www.youtube.com/watch?v=aHbE3pTyG-Q

	// bonus jakbyscie sie nudzili: zaimplementowac Swagger//

	// TODO 13.10.2023
	// Zaimplementuj Spring Basic Security w projekcie oraz model Usera, ktory moze byc zarowno zwyklym uzytkownikiem,
	// jak i administratorem. Administrator ma pelne uprawnienia w aplikacji (dostep do kontrolerow), natomiast user moze dokonac tylko
	// i wylacznie odczytu (nie moze nic modyfikowac ani usuwac). Wykorzystja do tego system Roles and Privileges.
	// w controllerach zastosujcie adnotacje @PreAuthorize

	// co na dockera? np apache kafka - komunikacja asynchroniczna miedzy np dwoma mikroserwisami
}
