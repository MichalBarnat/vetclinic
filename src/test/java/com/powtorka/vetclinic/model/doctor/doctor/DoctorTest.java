package com.powtorka.vetclinic.model.doctor.doctor;

import com.powtorka.vetclinic.model.doctor.Doctor;
import org.junit.Test;

import javax.print.Doc;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DoctorTest {

    @Test
    public void testEquals() {
        Doctor doctor1 = new Doctor();
        doctor1.setName("John");
        doctor1.setPesel("12345678901");

        Doctor doctor2 = new Doctor();
        doctor2.setName("John");
        doctor2.setPesel("12345678901");

        assertEquals(doctor1, doctor2);
        assertEquals(doctor1, doctor1);
        assertNotEquals(doctor1, null);
        assertNotEquals(doctor1, new Object());
    }

    @Test
    public void testHashCode() {
        Doctor doctor1 = new Doctor();
        doctor1.setName("John");
        doctor1.setPesel("12345678901");

        Doctor doctor2 = new Doctor();
        doctor2.setName("John");
        doctor2.setPesel("12345678901");

        assertEquals(doctor1.hashCode(), doctor2.hashCode());
    }

    @Test
    public void testGettersAndSetters() {
        Doctor doctor = new Doctor();
        doctor.setName("John");
        doctor.setPesel("12345678901");

        assertEquals("John", doctor.getName());
        assertEquals("12345678901", doctor.getPesel());
    }

    @Test
    public void testEqualsAndHashCode() {
        Doctor doctor1 = new Doctor();
        doctor1.setName("John");
        doctor1.setPesel("12345678901");

        Doctor doctor2 = new Doctor();
        doctor2.setName("John");
        doctor2.setPesel("12345678901");

        assertEquals(doctor1, doctor2);
        assertEquals(doctor1.hashCode(), doctor2.hashCode());

        Doctor doctor3 = new Doctor();
        doctor3.setName("Jane");
        doctor3.setPesel("12345678901");

        assertNotEquals(doctor1, doctor3);
        assertNotEquals(doctor1.hashCode(), doctor3.hashCode());
    }

    @Test
    public void testToString() {
        Doctor doctor = new Doctor(1L, "name", "surname", "speciality", "animalS", "o@o2.pl", 20, "12312312312");
        doctor.setName("John");
        doctor.setPesel("12345678901");

        System.out.println(doctor);

        String expected = "Doctor(id=1, name=John, surname=surname, speciality=speciality, animalSpeciality=animalS, email=o@o2.pl, rate=20, pesel=12345678901)";
        assertEquals(expected, doctor.toString());
    }

    @Test
    public void testToStringFromBuilder() {
        Doctor doctor = Doctor.builder()
                .id(1L)
                .name("michal")
                .surname("barnat")
                .speciality("s")
                .animalSpeciality("as")
                .email("michalbarnat@gmail.com")
                .rate(100)
                .pesel("12312312312")
                .build();

        String expectedString = "Doctor(id=1, name=michal, surname=barnat, speciality=s, animalSpeciality=as, email=michalbarnat@gmail.com, rate=100, pesel=12312312312)";
        String actualToString = doctor.toString();

        assertEquals(expectedString, actualToString);

    }

}