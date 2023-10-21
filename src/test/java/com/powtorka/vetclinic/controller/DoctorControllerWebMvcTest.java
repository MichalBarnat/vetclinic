package com.powtorka.vetclinic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.powtorka.vetclinic.config.SecurityConfig;
import com.powtorka.vetclinic.model.doctor.*;
import com.powtorka.vetclinic.repository.DoctorRepository;
import com.powtorka.vetclinic.service.DoctorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = DoctorController.class, useDefaultFilters = false,
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class))

public class DoctorControllerWebMvcTest {

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private DoctorService doctorService;

    @MockBean
    private DoctorRepository doctorRepository;

    @MockBean
    private Pageable pageable;

    private final MockMvc postman;
    private final ObjectMapper objectMapper;

    @Autowired
    public DoctorControllerWebMvcTest(MockMvc postman, ObjectMapper objectMapper) {
        this.postman = postman;
        this.objectMapper = objectMapper;
    }

    private Doctor doctor;
    private Doctor savedDoctor;
    private Doctor updatedDoctor;
    private Doctor partiallyUpdatedDoctor;
    private DoctorDto doctorDto;
    private DoctorDto expectedDto;
    private DoctorDto expectedUpdatedDto;
    private DoctorDto expectedPartiallyUpdateDto;
    private CreateDoctorCommand createDoctorCommand;
    private UpdateDoctorCommand updateDoctorCommand;
    private UpdateDoctorCommand partiallyUpdateDoctorCommand;

    @BeforeEach
    public void init() {
        doctor = Doctor.builder()
                .id(1L)
                .name("michal")
                .surname("barnat")
                .speciality("s")
                .animalSpeciality("as")
                .email("michalbarnat@gmail.com")
                .rate(100)
                .pesel("12312312312")
                .build();

        savedDoctor = Doctor.builder()
                .id(1L)
                .name("michal")
                .surname("barnat")
                .speciality("s")
                .animalSpeciality("as")
                .email("michalbarnat@gmail.com")
                .rate(100)
                .pesel("12312312312")
                .build();

        updatedDoctor = Doctor.builder()
                .id(1L)
                .name("updatedMichal")
                .surname("updatedBarnat")
                .speciality("updatedS")
                .animalSpeciality("updatedAs")
                .email("updatedMichalbarnat@gmail.com")
                .rate(200)
                .pesel("32132132132")
                .build();

        partiallyUpdatedDoctor = Doctor.builder()
                .id(1L)
                .name("new name")
                .surname("new surname")
                .speciality("s")
                .animalSpeciality("as")
                .email("michalbarnat@gmail.com")
                .rate(100)
                .pesel("12312312312")
                .build();

        doctorDto = DoctorDto.builder()
                .id(1L)
                .name("michal")
                .surname("barnat")
                .speciality("s")
                .animalSpeciality("as")
                .rate(100)
                .build();

        expectedDto = DoctorDto.builder()
                .id(1L)
                .name("michal")
                .surname("barnat")
                .speciality("s")
                .animalSpeciality("as")
                .rate(100)
                .build();

        expectedUpdatedDto = DoctorDto.builder()
                .id(1L)
                .name("updatedMichal")
                .surname("updatedBarnat")
                .speciality("updatedS")
                .animalSpeciality("updatedAs")
                .rate(200)
                .build();

        expectedPartiallyUpdateDto = DoctorDto.builder()
                .id(1L)
                .name("new name")
                .surname("new surname")
                .speciality("s")
                .animalSpeciality("as")
                .rate(100)
                .build();

        createDoctorCommand = CreateDoctorCommand.builder()
                .name("michal")
                .surname("barnat")
                .speciality("s")
                .animalSpeciality("as")
                .email("michalbarnat@gmail.com")
                .rate(100)
                .pesel("12312312312")
                .build();

        updateDoctorCommand = UpdateDoctorCommand.builder()
                .name("updatedMichal")
                .surname("updatedBarnat")
                .speciality("updatedS")
                .animalSpeciality("updatedAs")
                .email("updatedMichalbarnat@gmail.com")
                .rate(200)
                .pesel("32132132132")
                .build();

        partiallyUpdateDoctorCommand = UpdateDoctorCommand.builder()
                .name("new name")
                .surname("new surname")
                .build();

        pageable = PageRequest.of(0, 5);
    }

    @Test
    public void save_ShouldNotSaveWithoutAuthorization() throws Exception {
        when(modelMapper.map(any(CreateDoctorCommand.class), eq(Doctor.class)))
                .thenReturn(savedDoctor);
        when(doctorService.save(savedDoctor)).thenReturn(savedDoctor);
        when(modelMapper.map(savedDoctor, DoctorDto.class)).thenReturn(expectedDto);

        String requestBody = objectMapper.writeValueAsString(createDoctorCommand);

        postman.perform(post("/doctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        verify(doctorService, times(0)).save(savedDoctor);
        verify(modelMapper, times(0)).map(savedDoctor, DoctorDto.class);
    }

    @Test
    public void save_ShouldNotSaveWithWrongCredentials() throws Exception {
        when(modelMapper.map(any(CreateDoctorCommand.class), eq(Doctor.class)))
                .thenReturn(savedDoctor);
        when(doctorService.save(savedDoctor)).thenReturn(savedDoctor);
        when(modelMapper.map(savedDoctor, DoctorDto.class)).thenReturn(expectedDto);

        String requestBody = objectMapper.writeValueAsString(createDoctorCommand);

        postman.perform(post("/doctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .with(httpBasic("admin", "wrongpass")))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        verify(doctorService, times(0)).save(savedDoctor);
        verify(modelMapper, times(0)).map(savedDoctor, DoctorDto.class);
    }


    @Test
    public void save_ShouldNotSaveWithRoleUSER() throws Exception {

        when(modelMapper.map(any(CreateDoctorCommand.class), eq(Doctor.class)))
                .thenReturn(savedDoctor);
        when(doctorService.save(savedDoctor)).thenReturn(savedDoctor);
        when(modelMapper.map(savedDoctor, DoctorDto.class)).thenReturn(expectedDto);

        String requestBody = objectMapper.writeValueAsString(createDoctorCommand);

        postman.perform(post("/doctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .with(httpBasic("user", "pass")))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        verify(doctorService, times(0)).save(savedDoctor);
        verify(modelMapper, times(0)).map(savedDoctor, DoctorDto.class);
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"WRITE", "READ", "DELETE"})
    public void save_ShouldReturnStatusCreatedAndExpectedDoctorDtoWithRoleADMIN() throws Exception {

        when(modelMapper.map(any(CreateDoctorCommand.class), eq(Doctor.class)))
                .thenReturn(savedDoctor);
        when(doctorService.save(savedDoctor)).thenReturn(savedDoctor);
        when(modelMapper.map(savedDoctor, DoctorDto.class)).thenReturn(expectedDto);

        String requestBody = objectMapper.writeValueAsString(createDoctorCommand);

        postman.perform(post("/doctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                        //.with(httpBasic("admin", "admin")))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("michal"))
                .andExpect(jsonPath("$.surname").value("barnat"))
                .andExpect(jsonPath("$.speciality").value("s"))
                .andExpect(jsonPath("$.animalSpeciality").value("as"))
                .andExpect(jsonPath("$.rate").value(100));

        // Weryfikacja wywołań
        verify(doctorService).save(savedDoctor);
        verify(modelMapper).map(savedDoctor, DoctorDto.class);
    }


    @Test
    public void findById_ShouldReturnStatusOkAndExpectedDoctorDto() throws Exception {

        when(doctorService.findById(1L)).thenReturn(doctor);

        when(modelMapper.map(doctor, DoctorDto.class)).thenReturn(doctorDto);

        postman.perform(get("/doctor/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("michal"))
                .andExpect(jsonPath("$.surname").value("barnat"))
                .andExpect(jsonPath("$.speciality").value("s"))
                .andExpect(jsonPath("$.animalSpeciality").value("as"))
                .andExpect(jsonPath("$.rate").value(100));
    }

    @Test
    public void findAll_ShouldReturnPageContainingDoctors() throws Exception {

        Page<Doctor> page = new PageImpl<>(List.of(doctor));

        when(doctorService.findAll(eq(pageable))).thenReturn(page);
        when(modelMapper.map(any(CreateDoctorPageCommand.class), eq(Pageable.class))).thenReturn(pageable);
        when(modelMapper.map(doctor, DoctorDto.class)).thenReturn(expectedDto);


        postman.perform(get("/doctor")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("michal"))
                .andExpect(jsonPath("$[0].surname").value("barnat"))
                .andExpect(jsonPath("$[0].speciality").value("s"))
                .andExpect(jsonPath("$[0].animalSpeciality").value("as"))
                .andExpect(jsonPath("$[0].rate").value(100));
    }

    @Test
    public void deleteById_ShouldReturnStatusNoContent() throws Exception {
        String requestBody = objectMapper.writeValueAsString(savedDoctor);

        when(modelMapper.map(any(CreateDoctorCommand.class), eq(Doctor.class)))
                .thenReturn(savedDoctor);
        when(doctorService.save(any(Doctor.class))).thenReturn(savedDoctor);
        when(modelMapper.map(savedDoctor, DoctorDto.class)).thenReturn(expectedDto);

        postman.perform(post("/doctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("michal"));

        postman.perform(delete("/doctor/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        when(doctorService.findById(1L)).thenReturn(null);

        postman.perform(get("/doctor/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(doctorService).deleteById(3L);
    }

    @Test
    public void deleteAll_ShouldReturnStatusNoContent() throws Exception {
        String requestBody = objectMapper.writeValueAsString(savedDoctor);

        when(modelMapper.map(any(CreateDoctorCommand.class), eq(Doctor.class)))
                .thenReturn(savedDoctor);
        when(doctorService.save(any(Doctor.class))).thenReturn(savedDoctor);
        when(modelMapper.map(savedDoctor, DoctorDto.class)).thenReturn(expectedDto);

        postman.perform(post("/doctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("michal"));


        postman.perform(delete("/doctor/deleteAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(doctorService).deleteAll();

        postman.perform(get("/doctor/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(doctorService).save(savedDoctor);
        verify(modelMapper).map(savedDoctor, DoctorDto.class);
    }


    @Test
    public void edit_ShouldReturnStatusOkAndExpectedDoctorDto() throws Exception {

        when(doctorService.editDoctor(eq(1L), any(UpdateDoctorCommand.class))).thenReturn(updatedDoctor);
        when(modelMapper.map(updatedDoctor, DoctorDto.class)).thenReturn(expectedUpdatedDto);

        String requestBody = objectMapper.writeValueAsString(updateDoctorCommand);

        postman.perform(put("/doctor/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("updatedMichal"))
                .andExpect(jsonPath("$.surname").value("updatedBarnat"))
                .andExpect(jsonPath("$.speciality").value("updatedS"))
                .andExpect(jsonPath("$.animalSpeciality").value("updatedAs"))
                .andExpect(jsonPath("$.rate").value(200));

        verify(doctorService).editDoctor(eq(1L), any(UpdateDoctorCommand.class));
        verify(modelMapper).map(updatedDoctor, DoctorDto.class);
    }

    @Test
    public void editPartially_ShouldReturnStatusOkAndExpectedDoctorDto() throws Exception {

        when(doctorService.editPartially(eq(1L), any(UpdateDoctorCommand.class))).thenReturn(partiallyUpdatedDoctor);
        when(modelMapper.map(partiallyUpdatedDoctor, DoctorDto.class)).thenReturn(expectedPartiallyUpdateDto);

        String requestBody = objectMapper.writeValueAsString(partiallyUpdateDoctorCommand);

        postman.perform(patch("/doctor/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("new name"))
                .andExpect(jsonPath("$.surname").value("new surname"))
                .andExpect(jsonPath("$.speciality").value("s"))
                .andExpect(jsonPath("$.animalSpeciality").value("as"))
                .andExpect(jsonPath("$.rate").value(100));

        verify(doctorService).editPartially(eq(1L), any(UpdateDoctorCommand.class));
        verify(doctorService).editPartially(eq(1L), argThat(command ->
                "new name".equals(command.getName()) &&
                        "new surname".equals(command.getSurname())));
        verify(modelMapper).map(partiallyUpdatedDoctor, DoctorDto.class);

        //verify(doctorService).editPartially(1L, partiallyUpdateDoctorCommand); -- ZLE
        //DLACZEGO NIE TAK??
        //Ponieważ wywala to błąd spowodowany różnicą między obiektami używanymi w rzeczywistym wywołaniu metody.
        //W przypadku obiektów, które są tworzone dynamicznie podczas testu (na przykład przez deserializację JSON),
        // nie będziesz mógł użyć konkretnego obiektu do weryfikacji, ponieważ nie będziesz miał dostępu do instancji obiektu,
        // który jest faktycznie używany podczas wywołania metody.
        //W takim przypadku, używanie any(UpdateDoctorCommand.class) jest odpowiednim podejściem, ponieważ pozwala to na weryfikację,
        // czy metoda została wywołana z dowolnym obiektem tego typu, niezależnie od tego, czy jest to ten sam obiekt instancji.

    }

    @Test
    public void getTopRated_ShouldReturnStatusOkAndExpectedDoctorDtoList() throws Exception {

        Doctor qualifiedDoctor = Doctor.builder()
                .id(3L)
                .name("michal3")
                .surname("barnat3")
                .speciality("s3")
                .animalSpeciality("as3")
                .email("michalbarnat3@gmail.com")
                .rate(80)
                .pesel("12312312312")
                .build();

        List<Doctor> topRatedDoctors = List.of(doctor, qualifiedDoctor);

        when(doctorService.findDoctorsWithRateGreaterThan(80)).thenReturn(topRatedDoctors);

        when(modelMapper.map(doctor, DoctorDto.class)).thenReturn(expectedDto);

        postman.perform(get("/doctor/top-rated")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("michal"))
                .andExpect(jsonPath("$[0].surname").value("barnat"))
                .andExpect(jsonPath("$[0].speciality").value("s"))
                .andExpect(jsonPath("$[0].animalSpeciality").value("as"))
                .andExpect(jsonPath("$[0].rate").value(100))
                .andExpect(jsonPath("$[1].id").value(3))
                .andExpect(jsonPath("$[1].name").value("michal3"))
                .andExpect(jsonPath("$[1].surname").value("barnat3"))
                .andExpect(jsonPath("$[1].speciality").value("s3"))
                .andExpect(jsonPath("$[1].animalSpeciality").value("as3"))
                .andExpect(jsonPath("$[1].rate").value(80));

        verify(doctorService).findDoctorsWithRateGreaterThan(80);

    }

    @Test
    public void getTopRatedMin90_ShouldReturnStatusOkAndExpectedDoctorDtoList() throws Exception {
        List<Doctor> topRatedDoctors = List.of(doctor);

        when(doctorService.findDoctorsWithRateGreaterThan(90)).thenReturn(topRatedDoctors);

        when(modelMapper.map(doctor, DoctorDto.class)).thenReturn(expectedDto);

        postman.perform(get("/doctor/top-rated?minRate=90")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("michal"))
                .andExpect(jsonPath("$[0].surname").value("barnat"))
                .andExpect(jsonPath("$[0].speciality").value("s"))
                .andExpect(jsonPath("$[0].animalSpeciality").value("as"))
                .andExpect(jsonPath("$[0].rate").value(100));

        verify(doctorService).findDoctorsWithRateGreaterThan(90);
    }

    @Test
    public void doctorNotFoundExceptionHandler_ShouldReturnStatusNotFound() throws Exception {

        postman.perform(get("/api/doctor/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
