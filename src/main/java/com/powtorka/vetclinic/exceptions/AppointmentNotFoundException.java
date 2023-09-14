package com.powtorka.vetclinic.exceptions;


public class AppointmentNotFoundException extends  RuntimeException{
    public AppointmentNotFoundException(){
    }

    public AppointmentNotFoundException(String message){
        super(message);
    }
}
