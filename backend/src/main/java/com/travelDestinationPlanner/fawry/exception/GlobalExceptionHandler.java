package com.travelDestinationPlanner.fawry.exception;



import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(TravelDestinationPlannerApiException.class)
    public ResponseEntity<?> handelApiException(TravelDestinationPlannerApiException ex) {
        HttpStatus status =
                ex.getHttpStatus() != null ? ex.getHttpStatus() : HttpStatus.SERVICE_UNAVAILABLE;
        ErrorResponse errorResponse =
                new ErrorResponse(status.value(), ex.getMessage(), new Date());
        return new ResponseEntity<>(errorResponse, status);
    }



    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequest(BadRequestException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                new Date()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
