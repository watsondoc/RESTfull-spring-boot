package com.test.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST)  // 400
public class PersonNameException extends RuntimeException{

    public PersonNameException(){
        super("Person field Name is Empty");
    }

}
