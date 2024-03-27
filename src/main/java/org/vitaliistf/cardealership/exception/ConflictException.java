package org.vitaliistf.cardealership.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An exception indicating a conflict.
 * This exception is typically thrown when the request conflicts with the current state of the server.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictException extends RuntimeException {

    /**
     * Constructs a new ConflictException with the specified detail message.
     *
     * @param message the detail message
     */
    public ConflictException(String message){
        super(message);
    }

}
