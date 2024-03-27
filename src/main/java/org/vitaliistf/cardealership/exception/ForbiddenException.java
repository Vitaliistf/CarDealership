package org.vitaliistf.cardealership.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An exception indicating forbidden access.
 * This exception is typically thrown when the user does not have permission to perform a certain operation.
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {

    /**
     * Constructs a new ForbiddenException with the specified detail message.
     *
     * @param message the detail message
     */
    public ForbiddenException(String message){
        super(message);
    }

}
