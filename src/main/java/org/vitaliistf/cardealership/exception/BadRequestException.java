package org.vitaliistf.cardealership.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An exception indicating a bad request.
 * This exception is typically thrown when the request cannot be fulfilled due to invalid data.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    /**
     * Constructs a new BadRequestException with the specified detail message.
     *
     * @param message the detail message
     */
    public BadRequestException(String message){
        super(message);
    }

}
