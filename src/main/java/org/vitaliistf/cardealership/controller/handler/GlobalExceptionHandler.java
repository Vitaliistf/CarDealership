package org.vitaliistf.cardealership.controller.handler;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.vitaliistf.cardealership.exception.BadRequestException;
import org.vitaliistf.cardealership.exception.ConflictException;
import org.vitaliistf.cardealership.exception.ForbiddenException;
import org.vitaliistf.cardealership.exception.NotFoundException;

/**
 * Global exception handler for the application.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles all uncaught exceptions.
     * @param ex The exception.
     * @param model The model to add error attributes.
     * @return The error view.
     */
    @ExceptionHandler(Exception.class)
    public String globalExceptionHandler(Exception ex, Model model) {
        model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        model.addAttribute("error", ex.getMessage());
        model.addAttribute("message", "There was an exception during processing.");
        return "/error";
    }

    /**
     * Handles method argument type mismatch exceptions and bad request exceptions.
     * @param ex The exception.
     * @param model The model to add error attributes.
     * @return The error view.
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class, BadRequestException.class})
    public String methodArgumentTypeMismatchExceptionHandler(Exception ex, Model model) {
        model.addAttribute("status", HttpStatus.BAD_REQUEST.value());
        model.addAttribute("error", ex.getMessage());
        return "/error";
    }

    /**
     * Handles forbidden exceptions.
     * @param ex The exception.
     * @param model The model to add error attributes.
     * @return The error view.
     */
    @ExceptionHandler(ForbiddenException.class)
    public String forbiddenExceptionHandler(Exception ex, Model model) {
        model.addAttribute("status", HttpStatus.FORBIDDEN.value());
        model.addAttribute("error", ex.getMessage());
        return "/error";
    }

    /**
     * Handles not found exceptions.
     * @param ex The exception.
     * @param model The model to add error attributes.
     * @return The error view.
     */
    @ExceptionHandler(NotFoundException.class)
    public String notFoundExceptionHandler(Exception ex, Model model) {
        model.addAttribute("status", HttpStatus.NOT_FOUND.value());
        model.addAttribute("error", ex.getMessage());
        return "/error";
    }

    /**
     * Handles conflict exceptions.
     * @param ex The exception.
     * @param model The model to add error attributes.
     * @return The error view.
     */
    @ExceptionHandler(ConflictException.class)
    public String conflictExceptionHandler(Exception ex, Model model) {
        model.addAttribute("status", HttpStatus.CONFLICT.value());
        model.addAttribute("error", ex.getMessage());
        return "/error";
    }

}

