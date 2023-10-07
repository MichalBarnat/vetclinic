package com.powtorka.vetclinic.controller;

import com.powtorka.vetclinic.model.doctor.*;
import com.powtorka.vetclinic.service.DoctorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DoctorController.class)
public class DoctorControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DoctorService doctorService;

    @MockBean
    private ModelMapper modelMapper;

    @Test
    public void findById_ShouldReturnStatusOk() throws Exception {
        when(doctorService.findById(anyLong())).thenReturn(new Doctor());
        DoctorDto doctorDto = DoctorDto.builder()
                .id(1L)
                .name("name")
                .build();
        when(modelMapper.map(any(Doctor.class), any())).thenReturn(doctorDto);

        mockMvc.perform(get("/doctor/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void save_ShouldReturnStatusCreated() throws Exception {
        when(doctorService.save(any(Doctor.class))).thenReturn(new Doctor());
        when(modelMapper.map(any(CreateDoctorCommand.class), any())).thenReturn(new Doctor());
        DoctorDto doctorDto = DoctorDto.builder()
                .id(1L)
                .name("name")
                .build();
        when(modelMapper.map(any(Doctor.class), any())).thenReturn(doctorDto);

        mockMvc.perform(post("/doctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isCreated());
    }



}
