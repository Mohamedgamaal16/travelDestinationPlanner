package com.travelDestinationPlanner.fawry.exception;

import org.springframework.http.HttpStatus;

public class TravelDestinationPlannerApiException extends RuntimeException {

    private final HttpStatus httpStatus;

    public TravelDestinationPlannerApiException(String message) {
        super(message);
        this.httpStatus = null;
    }

    public TravelDestinationPlannerApiException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public TravelDestinationPlannerApiException() {
        super();
        this.httpStatus = null;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}