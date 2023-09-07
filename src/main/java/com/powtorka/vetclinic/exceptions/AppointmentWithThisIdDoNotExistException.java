package com.powtorka.vetclinic.exceptions;


public class AppointmentWithThisIdDoNotExistException extends  RuntimeException{
    public AppointmentWithThisIdDoNotExistException(){
    }

    public AppointmentWithThisIdDoNotExistException(String message){
        super(message);
    }
}
