package com.powtorka.vetclinic.model.doctor.doctor;

import com.powtorka.vetclinic.model.doctor.DoctorDto;
import org.junit.Test;

import static org.junit.Assert.*;

public class DoctorDtoTest {

    @Test
    public void testBuilder() {
        DoctorDto doctorDto = DoctorDto.builder()
                .id(1L)
                .name("name")
                .build();

        assertEquals("name", doctorDto.getName());
    }

}