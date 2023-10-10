package com.powtorka.vetclinic.model.appointment;


import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class AppointmentTest {



    @Test
    public void testEquals(){
        Appointment appointment1 = new Appointment();
        appointment1.setPrice(20.5);
        appointment1.setDateTime(LocalDateTime.parse("2023-08-31T20:26:03.93"));

        Appointment appointment2 = new Appointment();
        appointment2.setPrice(20.5);
        appointment2.setDateTime(LocalDateTime.parse("2023-08-31T20:26:03.93"));

        assertEquals(appointment1,appointment2);
        assertEquals(appointment1,appointment1);
        assertNotEquals(appointment1,null);
        assertNotEquals(appointment1,new Object());
    }

    @Test
    public  void testHashCode(){
        Appointment appointment1 = new Appointment();
        appointment1.setPrice(20.5);
        appointment1.setDateTime(LocalDateTime.parse("2023-08-31T20:26:03.93"));

        Appointment appointment2 = new Appointment();
        appointment2.setPrice(20.5);
        appointment2.setDateTime(LocalDateTime.parse("2023-08-31T20:26:03.93"));

        assertEquals(appointment1.hashCode(), appointment2.hashCode());
    }

    @Test
    public void testGettersAndSetters() {
        Appointment appointment = new Appointment();
        appointment.setPrice(20.5);
        appointment.setDateTime(LocalDateTime.parse("2023-08-31T20:26:03.93"));

        assertEquals(20.5, appointment.getPrice(),0);
        assertEquals(LocalDateTime.parse("2023-08-31T20:26:03.93"),appointment.getDateTime());
    }

    @Test
    public void testEqualsAndHashCode() {
        Appointment appointment1 = new Appointment();
        appointment1.setPrice(20.5);
        appointment1.setDateTime(LocalDateTime.parse("2023-08-31T20:26:03.93"));

        Appointment appointment2 = new Appointment();
        appointment2.setPrice(20.5);
        appointment2.setDateTime(LocalDateTime.parse("2023-08-31T20:26:03.93"));

        assertEquals(appointment1,appointment2);
        assertEquals(appointment1.hashCode(),appointment2.hashCode());

        Appointment appointment3 = new Appointment();
        appointment3.setPrice(21.5);
        appointment3.setDateTime(LocalDateTime.parse("2023-08-31T20:26:03.93"));

        assertNotEquals(appointment1,appointment3);
        assertNotEquals(appointment1.hashCode(),appointment3.hashCode());
    }

//    @Test
//    public void testToString() {
//        Doctor doctor = new Doctor();
//        doctor.setId(1L);
//        Patient patient = new Patient();
//        patient.setId(1L);
//        Appointment appointment = new Appointment(1L,doctorService.findById(1),patientService.findById(1),LocalDateTime.parse("2023-08-31T20:26:03.93"),20);
//    }




}