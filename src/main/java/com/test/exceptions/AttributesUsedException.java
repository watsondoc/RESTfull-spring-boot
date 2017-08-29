package com.test.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.FORBIDDEN)  // 403
public class AttributesUsedException extends RuntimeException {

    public AttributesUsedException(String message){
        super(message);
    }

}