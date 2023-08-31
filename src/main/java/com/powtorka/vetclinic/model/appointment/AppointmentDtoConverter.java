package com.powtorka.vetclinic.model.appointment;

import com.powtorka.vetclinic.model.doctor.Doctor;
import com.powtorka.vetclinic.model.mapper.ModelMapper;
import com.powtorka.vetclinic.model.patient.Patient;
import com.powtorka.vetclinic.service.DoctorService;
import com.powtorka.vetclinic.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppointmentDtoConverter {

  /*  @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

   */
/*
    public Appointment convertToEntity(CreateAppointmentDto dto) {
        Appointment appointment = modelMapper.map(dto, Appointment.class);

        Doctor doctor = doctorService.findById(dto.getDoctorId());
        Patient patient = patientService.findById(dto.getPatientId());

        appointment.setDoctor(doctor);
        appointment.setPatient(patient);

        return appointment;
    }

    public void updateEntity(EditAppointmentDto dto, Appointment appointment) {
        modelMapper.map(dto, appointment);
    }
 */
}

