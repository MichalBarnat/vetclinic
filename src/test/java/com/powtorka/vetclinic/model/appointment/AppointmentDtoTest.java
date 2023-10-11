package com.powtorka.vetclinic.model.appointment;

import org.junit.Test;

import static org.junit.Assert.*;

public class AppointmentDtoTest {

    @Test
    public  void  testBuilder(){
        AppointmentDto appointmentDto = AppointmentDto.builder()
                .id(1L)
                .price(20.5)
                .build();

        assertEquals(20.5,appointmentDto.getPrice(),0);
    }


}