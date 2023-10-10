package com.powtorka.vetclinic.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.powtorka.vetclinic.exceptions.DoctorNotFoundException;
import com.powtorka.vetclinic.model.doctor.*;
import com.powtorka.vetclinic.model.patient.*;
import com.powtorka.vetclinic.repository.DoctorRepository;
import com.powtorka.vetclinic.repository.PatientRepository;
import com.powtorka.vetclinic.service.DoctorService;
import com.powtorka.vetclinic.service.PatientService;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static java.util.Optional.empty;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PatientController.class)
public class PatientControllerWebMvcTest {

    @Autowired
    private MockMvc postman;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private PatientService patientService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PatientRepository patientRepository;

    @MockBean
    private Pageable mockedPageable;

    private Patient patient;
    private Patient savedPatient;
    private Patient udpatedPatient;
    private Patient partiallyUdpatedPatient;
    private PatientDto patientDto;
    private PatientDto expectedDto;
    private PatientDto expectedUdpatedDto;
    private PatientDto expectedPartiallyUdpateDto;
    private CreatePatientCommand createPatientCommand;
    private UdpatePatientCommand udpatePatientCommand;
    private UdpatePatientCommand partiallyUdpatePatientCommand;

    @BeforeEach
    public void init() {
        patient = Patient.builder()
                .id(1L)
                .name("Tyson")
                .species("Species")
                .breed("Breed")
                .ownerName("Krystian")
                .ownerEmail("krystian@gmail.com")
                .age(5)
                .build();

        savedPatient = Patient.builder()
                .id(1L)
                .name("Tyson")
                .species("Species")
                .breed("Breed")
                .ownerName("Krystian")
                .ownerEmail("krystian@gmail.com")
                .age(5)
                .build();

        udpatedPatient = Patient.builder()
                .id(1L)
                .name("udpatedTyson")
                .species("udpatedSpecies")
                .breed("udpatedBreed")
                .ownerName("udpatedKrystian")
                .ownerEmail("udpatedkrystian@gmail.com")
                .age(6)
                .build();

        partiallyUdpatedPatient = Patient.builder()
                .id(1L)
                .name("Bono")
                .species("Species")
                .breed("Breed")
                .ownerName("Robert")
                .ownerEmail("robert@gmail.com")
                .age(5)
                .build();

        patientDto = PatientDto.builder()
                .id(1L)
                .name("Tyson")
                .species("Species")
                .breed("Breed")
                .ownerName("Krystian")
                .age(5)
                .build();

        expectedDto = PatientDto.builder()
                .id(1L)
                .name("Tyson")
                .species("Species")
                .breed("Breed")
                .ownerName("Krystian")
                .age(5)
                .build();

        expectedUdpatedDto = PatientDto.builder()
                .id(1L)
                .name("udpateTyson")
                .species("udpateSpecies")
                .breed("udpateBreed")
                .ownerName("udpateKrystian")
                .age(6)
                .build();

        expectedPartiallyUdpateDto = PatientDto.builder()
                .id(1L)
                .name("Bono")
                .species("Species")
                .breed("Breed")
                .ownerName("Robert")
                .age(5)
                .build();
        createPatientCommand = CreatePatientCommand.builder()
                .name("Tyson")
                .species("Species")
                .breed("Breed")
                .ownerName("Krystian")
                .ownerEmail("krystian@gmail.com")
                .age(5)
                .build();

        udpatePatientCommand = UdpatePatientCommand.builder()
                .name("udpateTyson")
                .species("udpateSpecies")
                .breed("udpateBreed")
                .ownerName("udpateKrystian")
                .ownerEmail("udpateKrystian@gmail.com")
                .age(6)
                .build();

        partiallyUdpatePatientCommand = UdpatePatientCommand.builder()
                .name("Bono")
                .ownerName("Robert")
                .build();

    }

    @Test
    public void save_ShouldReturnStatusCreatedAndExpectedPatientDto() throws Exception {

        when(modelMapper.map(any(CreatePatientCommand.class), eq(Patient.class))).thenReturn(savedPatient);
        when(patientService.save(savedPatient)).thenReturn(savedPatient);
        when(modelMapper.map(savedPatient, PatientDto.class)).thenReturn(expectedDto);

        String requestBody = new ObjectMapper().writeValueAsString(createPatientCommand);

        postman.perform(post("/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Tyson"))
                .andExpect(jsonPath("$.species").value("Species"))
                .andExpect(jsonPath("$.breed").value("Breed"))
                .andExpect(jsonPath("$.ownerName").value("Krystian"))
                .andExpect(jsonPath("$.age").value(5));

        verify(patientService).save(savedPatient);
        verify(modelMapper).map(savedPatient, PatientDto.class);
    }

    @Test
    public void findById_ShouldReturnStatusIsOkAndExpectedDoctorDto() throws Exception {
        when(patientService.findById(1L)).thenReturn(patient);
        when(modelMapper.map(patient, PatientDto.class)).thenReturn(patientDto);

        postman.perform(get("/patient/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Tyson"))
                .andExpect(jsonPath("$.species").value("Species"))
                .andExpect(jsonPath("$.breed").value("Breed"))
                .andExpect(jsonPath("$.ownerName").value("Krystian"))
                .andExpect(jsonPath("$.age").value(5));
    }

    @Test
    public void findAll_ShouldReturnPageContainingPatients() throws Exception {Page<Patient> page = new PageImpl<>(List.of(patient));
        Pageable mockedPageable = PageRequest.of(0, 10);

        when(patientService.findAll(eq(mockedPageable))).thenReturn(page);
        when(modelMapper.map(any(CreatePatientPageCommand.class), eq(Pageable.class))).thenReturn(mockedPageable);
        when(modelMapper.map(patient, PatientDto.class)).thenReturn(expectedDto);

        postman.perform(get("/patient")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Tyson"))
                .andExpect(jsonPath("$[0].species").value("Species"))
                .andExpect(jsonPath("$[0].breed").value("Breed"))
                .andExpect(jsonPath("$[0].ownerName").value("Krystian"))
                .andExpect(jsonPath("$[0].age").value(5));
    }

    @Test
    public void deleteById_ShouldReturnStatusNoContent() throws Exception {
        String requestBody = objectMapper.writeValueAsString(savedPatient);

        when(modelMapper.map(any(CreatePatientCommand.class), eq(Patient.class)))
                .thenReturn(savedPatient);
        when(patientService.save(any(Patient.class))).thenReturn(savedPatient);
        when(modelMapper.map(savedPatient, PatientDto.class)).thenReturn(expectedDto);

        postman.perform(post("/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Tyson"));

        postman.perform(delete("/patient/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        when(patientService.findById(1L)).thenReturn(null);

        postman.perform(get("/patient/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(patientService).deleteById(3L);
    }

    @Test
    public void deleteAll_ShouldReturnStatusNoContent() throws Exception {
        String requestBody = objectMapper.writeValueAsString(savedPatient);

        when(modelMapper.map(any(CreatePatientCommand.class), eq(Patient.class)))
                .thenReturn(savedPatient);
        when(patientService.save(any(Patient.class))).thenReturn(savedPatient);
        when(modelMapper.map(savedPatient, PatientDto.class)).thenReturn(expectedDto);

        postman.perform(post("/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Tyson"));


        postman.perform(delete("/patient/deleteAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(patientService).deleteAll();

        postman.perform(get("/patient/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(patientService).save(savedPatient);
        verify(modelMapper).map(savedPatient, PatientDto.class);
    }

    @Test
    public void edit_ShouldReturnStatusOkAndExpectedPatientDto() throws Exception {

        when(patientService.editPatient(eq(1L), any(UdpatePatientCommand.class))).thenReturn(udpatedPatient);
        when(modelMapper.map(udpatedPatient, PatientDto.class)).thenReturn(expectedUdpatedDto);

        String requestBody = new ObjectMapper().writeValueAsString(udpatePatientCommand);

        postman.perform(put("/patient/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("udpateTyson"))
                .andExpect(jsonPath("$.species").value("udpateSpecies"))
                .andExpect(jsonPath("$.breed").value("udpateBreed"))
                .andExpect(jsonPath("$.ownerName").value("udpateKrystian"))
                .andExpect(jsonPath("$.age").value(6));

        verify(patientService).editPatient(eq(1L), any(UdpatePatientCommand.class));
        verify(modelMapper).map(udpatedPatient, PatientDto.class);
    }

    @Test
    public void editPartially_ShouldReturnStatusOkAndExpectedPatientDto() throws Exception {

        when(patientService.editPartially(eq(1L), any(UdpatePatientCommand.class))).thenReturn(partiallyUdpatedPatient);
        when(modelMapper.map(partiallyUdpatedPatient, PatientDto.class)).thenReturn(expectedPartiallyUdpateDto);

        String requestBody = objectMapper.writeValueAsString(partiallyUdpatePatientCommand);

        postman.perform(patch("/patient/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Bono"))
                .andExpect(jsonPath("$.species").value("Species"))
                .andExpect(jsonPath("$.breed").value("Breed"))
                .andExpect(jsonPath("$.ownerName").value("Robert"))
                .andExpect(jsonPath("$.age").value(5));

        verify(patientService).editPartially(eq(1L), any(UdpatePatientCommand.class));
        verify(patientService).editPartially(eq(1L), argThat(command ->
                "Bono".equals(command.getName()) &&
                        "Robert".equals(command.getOwnerName())));
        verify(modelMapper).map(partiallyUdpatedPatient, PatientDto.class);
    }

//    @Test
//    public void getTopAge_ShouldReturnStatusOkAndExpectedPatientDtoList() throws Exception {
//
//        Patient qualifiedPatient = Patient.builder()
//                .id(3L)
//                .name("Tyson3")
//                .species("Species3")
//                .breed("Breed3")
//                .ownerName("Krystian3")
//                .ownerEmail("krystian3@gmail.com")
//                .age(5)
//                .build();
//
//        List<Patient> theOldestPatients = List.of(patient, qualifiedPatient);
//
//        when(patientService.findPatientsWithAgeGreaterThan(5)).thenReturn(theOldestPatients);
//        when(modelMapper.map(patient, PatientDto.class)).thenReturn(expectedDto);
//
//        postman.perform(get("/patient/the-oldest")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].id").value(1))
//                .andExpect(jsonPath("$[0].name").value("Tyson"))
//                .andExpect(jsonPath("$[0].species").value("Species"))
//                .andExpect(jsonPath("$[0].breed").value("Breed"))
//                .andExpect(jsonPath("$[0].ownerName").value("Krystian"))
//                .andExpect(jsonPath("$[0].age").value(7))
//
//                .andExpect(jsonPath("$[1].id").value(3))
//                .andExpect(jsonPath("$[1].name").value("Tyson3"))
//                .andExpect(jsonPath("$[1].species").value("Species3"))
//                .andExpect(jsonPath("$[1].breed").value("Breed3"))
//                .andExpect(jsonPath("$[1].ownerName").value("Krystian3"))
//                .andExpect(jsonPath("$[1].age").value(5));
//
//        verify(patientService).findPatientsWithAgeGreaterThan(5);
//    }


}
