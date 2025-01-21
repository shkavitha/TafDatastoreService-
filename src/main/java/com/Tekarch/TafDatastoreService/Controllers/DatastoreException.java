package com.Tekarch.TafDatastoreService.Controllers;

public class DatastoreException extends RuntimeException{

    public DatastoreException(String message){
        super(message);
    }

    public DatastoreException(String message, Throwable cause){
        super(message, cause);
    }
}
