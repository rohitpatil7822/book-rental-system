
package com.exceptions.rentread.exceptions;
import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends RuntimeException{
    
    private HttpStatus status;

    public ResourceNotFoundException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getHttpStatus(){

        return this.status;
    }

}

