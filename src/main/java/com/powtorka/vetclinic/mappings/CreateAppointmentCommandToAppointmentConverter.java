package com.powtorka.vetclinic.mappings;

import com.powtorka.vetclinic.model.appointment.Appointment;
import com.powtorka.vetclinic.model.appointment.CreateAppointmentCommand;
import com.powtorka.vetclinic.model.doctor.Doctor;
import com.powtorka.vetclinic.model.patient.Patient;
import com.powtorka.vetclinic.service.DoctorService;
import com.powtorka.vetclinic.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateAppointmentCommandToAppointmentConverter implements Converter<CreateAppointmentCommand, Appointment> {

    private final DoctorService doctorService;
    private final PatientService patientService;


    @Override
    public Appointment convert(MappingContext<CreateAppointmentCommand, Appointment> mappingContext) {
        CreateAppointmentCommand command = mappingContext.getSource();
        Patient patient = patientService.findById(command.getPatientId());
        Doctor doctor = doctorService.findById(command.getDoctorId());
        return Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .dateTime(command.getDateTime())
                .price(command.getPrice())
                .build();
    }
}
