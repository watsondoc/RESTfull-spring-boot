package com.test.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.FORBIDDEN)  // 403
public class PersonsContainsEntity extends RuntimeException {

    public PersonsContainsEntity() {
        super();
    }

    public PersonsContainsEntity(String message){
        super(message);
    }

}