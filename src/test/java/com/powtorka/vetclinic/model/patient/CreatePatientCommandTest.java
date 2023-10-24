//package com.powtorka.vetclinic.model.doctor.patient;
//
//import com.powtorka.vetclinic.model.patient.command.CreatePatientCommand;
//import org.junit.Test;
//import org.junit.jupiter.api.BeforeEach;
//
//import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class CreatePatientCommandTest {
//    private CreatePatientCommand createPatientCommand;
//
//    @BeforeEach
//    public void setUp() {
//        createPatientCommand = new CreatePatientCommand();
//    }
//
//    @Test
//    public void noArgsConstructorTest() {
//        assertNotNull(createPatientCommand);
//    }
//
//    @Test
//    public void allArgsConstructorTest() {
//        CreatePatientCommand command = new CreatePatientCommand("Max", "Dog", "Labrador", "John Doe", "john.doe@example.com", 5);
//        assertNotNull(command);
//        assertEquals("Max", command.getName());
//        assertEquals("Dog", command.getSpecies());
//        assertEquals("Labrador", command.getBreed());
//        assertEquals("John Doe", command.getOwnerName());
//        assertEquals("john.doe@example.com", command.getOwnerEmail());
//        assertEquals(5, command.getAge());
//    }
//
//    @Test
//    public void settersAndGettersTest() {
//        createPatientCommand.setName("Buddy");
//        createPatientCommand.setSpecies("Cat");
//        createPatientCommand.setBreed("Persian");
//        createPatientCommand.setOwnerName("Alice");
//        createPatientCommand.setOwnerEmail("alice@example.com");
//        createPatientCommand.setAge(3);
//
//        assertEquals("Buddy", createPatientCommand.getName());
//        assertEquals("Cat", createPatientCommand.getSpecies());
//        assertEquals("Persian", createPatientCommand.getBreed());
//        assertEquals("Alice", createPatientCommand.getOwnerName());
//        assertEquals("alice@example.com", createPatientCommand.getOwnerEmail());
//        assertEquals(3, createPatientCommand.getAge());
//    }
//
//    @Test
//    public void builderTest() {
//        CreatePatientCommand command = CreatePatientCommand.builder()
//                .name("Rex")
//                .species("Dog")
//                .breed("German Shepherd")
//                .ownerName("Bob")
//                .ownerEmail("bob@example.com")
//                .age(4)
//                .build();
//
//        assertNotNull(command);
//        assertEquals("Rex", command.getName());
//        assertEquals("Dog", command.getSpecies());
//        assertEquals("German Shepherd", command.getBreed());
//        assertEquals("Bob", command.getOwnerName());
//        assertEquals("bob@example.com", command.getOwnerEmail());
//        assertEquals(4, command.getAge());
//    }
//}
